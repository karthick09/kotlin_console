class Database private constructor() {
    var owner: Owner? = null
    val managerList: ArrayList<Manger>
        get() = Companion.managerList
    val salesManList: ArrayList<SalesMan>
        get() = Companion.salesManList
    var itemList: ArrayList<Item>
        get() = Companion.itemList
        set(newItemList) {
            Companion.itemList = newItemList
        }
    var salesList: HashMap<Item, Float>
        get() = Companion.salesList
        set(newSalesList) {
            Companion.salesList = newSalesList
        }
    var purchaseList: HashMap<Item, Float>
        get() = Companion.purchaseList
        set(newPurchaseList) {
            Companion.purchaseList = newPurchaseList
        }

    fun addManager(manager: Manger) {
        Companion.managerList.add(manager)
    }

    fun addSaleMan(salesman: SalesMan) {
        Companion.salesManList.add(salesman)
    }

    fun addItem(item: Item) {
        Companion.itemList.add(item)
    }

    fun removeItem(item: Item) {
        Companion.itemList.remove(item)
    }

    fun addSales(id: Item) {
        Companion.salesList[id] = 0.0.toFloat()
    }

    fun addPurchase(id: Item, quantity: Float) {
        Companion.purchaseList[id] = quantity
    }

    companion object {
        private var database: Database? = null
        val instance: Database?
            get() {
                if (database == null) {
                    database = Database()
                }
                return database
            }
        private var itemList = ArrayList<Item>()
        private val managerList = ArrayList<Manger>()
        private val salesManList = ArrayList<SalesMan>()
        private var salesList = java.util.HashMap<Item, Float>()
        private var purchaseList = java.util.HashMap<Item, Float>()
    }
}