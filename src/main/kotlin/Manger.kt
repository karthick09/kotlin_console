import java.util.*

class Manger(name: String?, userId: String?, email: String?, phone: String?, account: Account?, userType: UserType?) :
    User(name!!, userId!!, email!!, phone!!, account!!, userType!!), InventoryControl {
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

    fun showItemList() {
        DatabaseManager.itemList
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
        val updateQuantity: Float
        val item: Item = DatabaseManager.getItem(itemId)!!
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
            DatabaseManager.updateItem(quantity, updateQuantity, item, userType, "sales")
        } else {
            println("insufficient stock")
            println("available stock is " + item.quantity)
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
            DatabaseManager.updateItem(quantity, updateQuantity, item, userType, "purchase")
        } else {
            println("item not found")
        }
    }
}
