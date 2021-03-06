class class_variable_access_1_donor(var size: Int) {
    val data: Int = 5
}

class class_variable_access_1_owner(val arg: class_variable_access_1_donor) {
    val tKIopsD = 56
    fun getSize(): Int {
        return arg.size + 8
    }
}


fun class_variable_access_1_test(x: Int): Int {
    val instance_donor = class_variable_access_1_donor(x)
    val instance_owner = class_variable_access_1_owner(instance_donor)

    return instance_owner.getSize()
}