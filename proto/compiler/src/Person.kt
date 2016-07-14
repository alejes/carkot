class Person constructor (name: kotlin.String?, id: Int?, email: kotlin.String?, phones: Array <PhoneNumber> ) {
    var name : kotlin.String?
        private set

    var id : Int?
        private set

    var email : kotlin.String?
        private set

    var phones : Array <PhoneNumber>
        private set


    init {
        this.name = name
        this.id = id
        this.email = email
        this.phones = phones
    }
    enum class PhoneType(val ord: Int) {
        MOBILE (0),
        HOME (1),
        WORK (2);

        companion object {
            fun fromIntToPhoneType (ord: Int): PhoneType {
                return when (ord) {
                    0 -> PhoneType.MOBILE
                    1 -> PhoneType.HOME
                    2 -> PhoneType.WORK
                    else -> throw InvalidProtocolBufferException("Error: got unexpected int ${ord} while parsing PhoneType ");
                }
            }
        }
    }
    class PhoneNumber constructor (number: kotlin.String?, type: PhoneType?) {
        var number : kotlin.String?
            private set

        var type : PhoneType?
            private set


        init {
            this.number = number
            this.type = type
        }

        fun writeTo (output: CodedOutputStream) {
            writeToNoTag(output)
        }

        fun writeToNoTag (output: CodedOutputStream) {
            output.writeString (1, number)
            output.writeEnum (2, type?.ord)
        }

        class BuilderPhoneNumber constructor (number: kotlin.String?, type: PhoneType?) {
            var number : kotlin.String?

            var type : PhoneType?


            init {
                this.number = number
                this.type = type
            }

            fun readFrom (input: CodedInputStream) {
                readFromNoTag(input)
            }

            fun readFromNoTag (input: CodedInputStream) {
                number = input.readString(1)
                type = PhoneType.fromIntToPhoneType(input.readEnum(2))
            }

            fun build(): PhoneNumber {
                return PhoneNumber(number, type)
            }
        }

        fun mergeFrom (input: CodedInputStream) {
            number = input.readString(1)
            type = PhoneType.fromIntToPhoneType(input.readEnum(2))
        }
    }


    fun writeTo (output: CodedOutputStream) {
        writeToNoTag(output)
    }

    fun writeToNoTag (output: CodedOutputStream) {
        output.writeString (1, name)
        output.writeInt32 (2, id)
        output.writeString (3, email)
        if (phones.size > 0) {
            output.writeInt32NoTag(phones.size)
            for (item in phones) {
                item.writeToNoTag(output)
            }
        }
    }

    class BuilderPerson constructor (name: kotlin.String?, id: Int?, email: kotlin.String?, phones: Array <PhoneNumber> ) {
        var name : kotlin.String?

        var id : Int?

        var email : kotlin.String?

        var phones : Array <PhoneNumber>


        init {
            this.name = name
            this.id = id
            this.email = email
            this.phones = phones
        }

        fun readFrom (input: CodedInputStream) {
            readFromNoTag(input)
        }

        fun readFromNoTag (input: CodedInputStream) {
            name = input.readString(1)
            id = input.readInt32(2)
            email = input.readString(3)
            if (phones.size > 0) {
                val tag = input.readTag()
                val listSize = input.readInt32NoTag()
                for (i in 1..listSize) {
                    phones[i - 1].mergeFrom(input)
                }
            }
        }

        fun build(): Person {
            return Person(name, id, email, phones)
        }
    }

    fun mergeFrom (input: CodedInputStream) {
        name = input.readString(1)
        id = input.readInt32(2)
        email = input.readString(3)
        if (phones.size > 0) {
            val tag = input.readTag()
            val listSize = input.readInt32NoTag()
            for (i in 1..listSize) {
                phones[i - 1].mergeFrom(input)
            }
        }
    }
}