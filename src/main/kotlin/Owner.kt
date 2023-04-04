import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.regex.Pattern
import kotlin.math.roundToInt

class Owner(
    name: String?,
    userId: String?,
    email: String?,
    phone: String?,
    account: Account?,
    userType: UserType?,
    val password: String
) :
    User(name!!, userId!!, email!!, phone!!, account!!, userType!!), InventoryControl {
    override var userId = ""
    private var pass = ""
    override var name = ""
    override var email = ""
    override var phone = ""

    fun addManager() {
        println("enter the person details")
        println("enter the username")
        userId = validateUserName(1)
        println("enter the password")
        pass = validatePassword()
        println("enter the name ")
        name = validateName()
        println("enter the email")
        email = validateEmail()
        println("enter the phone")
        phone = validatePhoneNumber()
        val account = Account(userId, pass, AccountStatus.ACTIVE)
        println("enter the manager id")
        val mId = validateMId()
        val manager = Manger(name, mId, email, phone, account, UserType.MANAGER)
        DatabaseManager.assignManger(manager, password)
    }

    fun addSalesPerson() {
        println("enter the person details")
        println("enter the username")
        userId = validateUserName(2)
        println("enter the password")
        pass = validatePassword()
        println("enter the name ")
        name = validateName()
        println("enter the email")
        email = validateEmail()
        println("enter the phone")
        phone = validatePhoneNumber()
        val account = Account(userId, pass, AccountStatus.ACTIVE)
        println("enter the salesperson id ")
        val salePersonId = validateSId()
        val salesMan = SalesMan(name, salePersonId, email, phone, account, UserType.SALESMAN)
        DatabaseManager.assignSalesMan(salesMan, password)
    }

    fun salary() {
        val empId: String
        val sc = Scanner(System.`in`)
        println("1.manager\n2.salesman")
        val designation: Int = validateInteger()
        println("enter the employee id")
        empId = sc.next()
        var salary: Float
        if (designation == 1) {
            val manger: Manger? = DatabaseManager.getManger(empId)
            if (manger != null) {
                val doj: LocalDate = manger.dateOfJoin
                val presentDate = LocalDate.now()
                val noOfDays = ChronoUnit.DAYS.between(doj, presentDate)
                salary = noOfDays.toFloat() / 30
                salary = salary.roundToInt().toFloat()
                salary *= 20000
                println("salary of the emp is :$salary")
            } else {
                println("manager not found")
            }
        } else if (designation == 2) {
            val salesMan: SalesMan?= DatabaseManager.getSalesMan(empId)
            if (salesMan != null) {
                val doj = salesMan.dateOfJoin
                val presentDate = LocalDate.now()
                val noOfDays = ChronoUnit.DAYS.between(doj, presentDate)
                salary = noOfDays.toFloat() / 30
                salary *= 15000
                salary = salary.roundToInt().toFloat()
                println("salary of the emp is :$salary")
            } else {
                println("salesman not found")
            }
        } else {
            println("designation not found ")
        }
    }

    fun turnover(choice: Int) {
        when (choice) {
            1 -> {
                val salesList: HashMap<Item, Float> = DatabaseManager.salesList
                for ((item, value) in salesList) {
                    println("item id : ${item.itemId} item name : ${item.itemName} total sales count $value")
                }
            }
            2 -> {
                val purchaseList: HashMap<Item, Float> = DatabaseManager.purchaseList
                for ((item, value) in purchaseList) {
                    println("item id : ${item.itemId} item name : ${item.itemName} total purchase count $value")
                }
            }
            else -> {
                println("invalid choice")
            }
        }
    }

    private fun validateFloat(): Float {
        var flag: Boolean
        var float1 = 0f
        do {
            flag = true
            try {
                val scanner = Scanner(System.`in`)
                float1 = scanner.nextFloat()
            } catch (ignored: InputMismatchException) {
            }
            if (float1 == 0f) {
                println("enter a valid input:")
                flag = false
            }
        } while (!flag)
        return float1
    }

    override fun sales() {
        val sc = Scanner(System.`in`)
        println("enter the item no ")
        val itemId: String = sc.next()
        println("enter the no of quantity")
        val quantity: Float = validateFloat()
        val item: Item? = DatabaseManager.getItem(itemId)
        val updateQuantity: Float
        if (item != null) {
            if (quantity <= item.quantity) {
                var price: Float = quantity * item.itemPrice
                println("if any discount Y/N")
                val choice = sc.next()
                if (choice == "y") {
                    println("enter the discount percentage")
                    var discount = sc.nextFloat()
                    discount /= 100
                    price -= price * discount
                }
                println("the total amount rs:$price")
                updateQuantity = item.quantity - quantity
                DatabaseManager.updateItem(quantity, updateQuantity, item, UserType.OWNER, "sales")
            } else {
                println("insufficient stock")
                println("available stock is " + item.quantity)
            }
        } else {
            println("item not found")
        }
    }

    private fun validateItemId(): String {
        var string: String
        var isItemId: Boolean
        do {
            val scanner = Scanner(System.`in`)
            string = scanner.nextLine()
            isItemId = DatabaseManager.checkItemId(string)
        } while (!isItemId)
        return string
    }

    private fun validateItemName(): String {
        var string: String
        var isItemName: Boolean
        do {
            val scanner = Scanner(System.`in`)
            string = scanner.nextLine()
            isItemName = DatabaseManager.checkItemName(string)
        } while (!isItemName)
        return string
    }

    override fun addItem(id: String?) {
        println("enter the item id")
        val itemId: String = validateItemId()
        println("enter the item name")
        val iName: String = validateItemName()
        println("enter the price")
        val price: Float = validateFloat()
        println("enter the quantity")
        val quantity: Float = validateFloat()
        val item = Item(itemId, iName, price, quantity)
        if (id != null) {
            DatabaseManager.addItem(item, id)
        }
    }

    override fun deleteItem(id: String?) {
        val sc = Scanner(System.`in`)
        println("enter the item id")
        val itemId: String = sc.next()
        if (id != null) {
            DatabaseManager.deleteItem(itemId, id)
        }
    }

    override fun purchase() {
        val sc = Scanner(System.`in`)
        println("enter the item id")
        val id: String = sc.next()
        println("enter the count to add")
        val quantity: Float = validateFloat()
        val updateQuantity: Float
        val item: Item? = DatabaseManager.getItem(id)
        if (item != null) {
            updateQuantity = item.quantity + quantity
            DatabaseManager.updateItem(quantity, updateQuantity, item, UserType.OWNER, "purchase")
        } else {
            println("item not found")
        }
    }

    companion object {
        fun validateName(): String {
            var stringLength: Int
            var value = true
            var string: String
            var isAlphabet = true
            do {
                if (!value || !isAlphabet) {
                    println(
                        "(Input size should be greater than 4 and should not contain any numbers or special characters)" +
                                "Enter again:"
                    )
                }
                val scanner = Scanner(System.`in`)
                string = scanner.nextLine()
                for (element in string) {
                    isAlphabet = element.code in 0x41..0x5a || element.code in 0x61..0x7a || element.code == 0x20
                    if (!isAlphabet) break
                }
                stringLength = string.length
                value = stringLength >= 5
            } while (!value || !isAlphabet)
            return string
        }

        fun validateEmail(): String {
            var string: String
            var isEmail = true
            do {
                if (!isEmail) {
                    println("enter valid email")
                }
                val scanner = Scanner(System.`in`)
                string = scanner.nextLine()
                val emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                        "[a-zA-Z0-9_+&*-]+)*@" +
                        "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                        "A-Z]{2,7}$"
                val pat = Pattern.compile(emailRegex)
                isEmail = pat.matcher(string).matches()
            } while (!isEmail)
            return string
        }

        fun validateUserName(choice: Int): String {
            var stringLength: Int
            var string: String
            var value = true
            var isUserName: Boolean
            do {
                if (!value) {
                    println("(Input size should be greater than 4)" + "Enter again:")
                }
                val scanner = Scanner(System.`in`)
                string = scanner.nextLine()
                stringLength = string.length
                isUserName = DatabaseManager.checkUserName(string, choice)
                value = stringLength >= 5
            } while (!isUserName || !value)
            return string
        }

        fun validatePhoneNumber(): String {
            var stringLength: Int
            var value = true
            var string: String
            var isNumber = true
            do {
                if (!value || !isNumber) {
                    println(
                        "(Input size should be equal to 10 and contain only numbers)" +
                                "Enter again:"
                    )
                }
                val scanner = Scanner(System.`in`)
                string = scanner.nextLine()
                for (element in string) {
                    isNumber = element.code in 0x30..0x39
                    if (!isNumber) break
                }
                stringLength = string.length
                value = stringLength == 10
            } while (!value || !isNumber)
            return string
        }

        fun validateMId(): String {
            var string: String
            var isMID: Boolean
            do {
                val scanner = Scanner(System.`in`)
                string = scanner.nextLine()
                isMID = DatabaseManager.checkMId(string)
            } while (!isMID)
            return string
        }

        fun validateSId(): String {
            var string: String
            var isSID: Boolean
            do {
                val scanner = Scanner(System.`in`)
                string = scanner.nextLine()
                isSID = DatabaseManager.checkSId(string)
            } while (!isSID)
            return string
        }

        fun validatePassword(): String {
            var stringLength: Int
            var string: String
            var isPassword = true
            do {
                if (!isPassword) {
                    println(
                        "(Input size should be greater than 7)" +
                                "Enter again:"
                    )
                }
                val scanner = Scanner(System.`in`)
                string = scanner.nextLine()
                stringLength = string.length
                isPassword = stringLength >= 8
            } while (!isPassword)
            return string
        }

        fun validateInteger(): Int {
            var flag: Boolean
            var integer1 = 0
            do {
                flag = true
                try {
                    val scanner = Scanner(System.`in`)
                    integer1 = scanner.nextInt()
                } catch (ignored: InputMismatchException) {
                }
                if (integer1 == 0) {
                    println("enter integer only:")
                    flag = false
                }
            } while (!flag)
            return integer1
        }
    }
}
