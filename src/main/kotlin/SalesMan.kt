import java.util.*

class SalesMan(name: String?, userId: String?, email: String?, phone: String?, account: Account?, userType: UserType?) :
    User(name!!, userId!!, email!!, phone!!, account!!, userType!!) {
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
        val item: Item? = DatabaseManager.getItem(itemId)
        if (item != null) {
            if (quantity < item.quantity) {
                val price: Float = quantity * item.itemPrice
                println("the total amount rs:$price")
                updateQuantity = item.quantity - quantity
                DatabaseManager.updateItem(quantity, updateQuantity, item, userType, "sales")
            } else {
                println("insufficient stock")
                println("available stock is " + item.quantity)
            }
        } else {
            println("item not found")
        }
    }
}
