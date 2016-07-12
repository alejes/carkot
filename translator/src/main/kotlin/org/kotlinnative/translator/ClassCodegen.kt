package org.kotlinnative.translator

import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.descriptors.annotations.Annotations
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtParameter
import org.jetbrains.kotlin.psi.KtUserType
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.annotations.argumentValue
import org.kotlinnative.translator.exceptions.TranslationException
import org.kotlinnative.translator.llvm.LLVMBuilder
import org.kotlinnative.translator.llvm.LLVMClassVariable
import org.kotlinnative.translator.llvm.LLVMFunctionDescriptor
import org.kotlinnative.translator.llvm.LLVMVariable
import org.kotlinnative.translator.llvm.types.LLVMType
import org.kotlinnative.translator.llvm.types.LLVMVoidType
import org.kotlinnative.translator.llvm.types.parseLLVMType
import java.util.*

class ClassCodegen(val state: TranslationState, val clazz: KtClass, val codeBuilder: LLVMBuilder) {

    val annotation: Boolean
    val native: Boolean
    val fields = ArrayList<LLVMVariable>()
    val size: Int

    init {
        val descriptor = state.bindingContext.get(BindingContext.CLASS, clazz) ?: throw TranslationException()
        val parameterList = clazz.getPrimaryConstructorParameterList()!!.parameters

        var offset = 0
        var currentSize = 0
        annotation = descriptor.kind == ClassKind.ANNOTATION_CLASS

        if (!annotation) {
            for (field in parameterList) {
                val type = getNativeType(field) ?: parseLLVMType((field.typeReference?.typeElement as KtUserType).referencedName!!)
                val field = LLVMClassVariable(field.name!!, type, offset)
                fields.add(field)

                currentSize += type.size
                offset++
            }
        }

        native = isNative(descriptor.annotations)
        size = currentSize
    }

    fun generate() {
        if (annotation) {
            return
        }

        generateStruct()
        generateDefaultConstructor()
    }

    private fun generateStruct() {
        val name = clazz.name!!

        codeBuilder.createClass(name, fields)
    }

    private fun generateDefaultConstructor() {
        codeBuilder.addLLVMCode(LLVMFunctionDescriptor(clazz.name!!, fields, LLVMVoidType()))

        codeBuilder.addStartExpression()
        generateLoadArguments()
        codeBuilder.addEndExpression()
    }

    private fun generateLoadArguments() {
        fields.forEach {
            val loadVariable = LLVMVariable("%${it.label}", it.type, it.label)
            codeBuilder.loadVariable(loadVariable)
        }
    }

    private fun getNativeType(field: KtParameter): LLVMType? {
        for (annotation in field.annotationEntries) {
            val annotationDescriptor = state.bindingContext.get(BindingContext.ANNOTATION, annotation)
            val type = annotationDescriptor?.type.toString()
            if (type == "Native") {
                return parseLLVMType(annotationDescriptor!!.argumentValue("type").toString())
            }
        }

        return null
    }

    private fun isNative(annotations: Annotations?): Boolean {
        annotations ?: return false

        for (i in annotations) {
            if (i.type.toString() == "Native") {
                return true
            }
        }

        return false
    }

}