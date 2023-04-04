object DatabaseManager {
    private var database: Database = Database.instance!!
    fun getManger(id: String): Manger? {
        for (m in database.managerList) {
            val account = m.account
            if (id == account.id || id == m.userId) {
                return m
            }
        }
        return null
    }

    fun getSalesMan(id: String): SalesMan? {
        for (sm in database.salesManList) {
            val account = sm.account
            if (id == account.id || id == sm.userId) {
                return sm
            }
        }
        return null
    }

    val salesList: HashMap<Item, Float>
        get() = database.salesList
    val purchaseList: HashMap<Item, Float>
        get() = database.purchaseList

    fun checkUserName(username: String, choice: Int): Boolean {
        var flag = true
        var account: Account
        if (choice == 1) {
            for (p in database.managerList) {
                account = p.account
                if (account.id == username) {
                    println("User name already exist!")
                    flag = false
                    break
                }
            }
        }
        if (choice == 2) {
            for (p in database.salesManList) {
                account = p.account
                if (account.id == username) {
                    println("User name already exist!")
                    flag = false
                    break
                }
            }
        }
        return flag
    }

    fun checkItemName(itemName: String): Boolean {
        var flag = true
        for (item in database.itemList) {
            if (item.itemName == itemName) {
                println("item name already exits")
                flag = false
                break
            }
        }
        return flag
    }

    fun checkItemId(itemId: String): Boolean {
        var flag = true
        for (item in database.itemList) {
            if (item.itemId == itemId) {
                println("item id already exits")
                flag = false
                break
            }
        }
        return flag
    }

    fun checkMId(mID: String): Boolean {
        var flag = true
        for (manger in database.managerList) {
            if (manger.userId == mID) {
                println("manager id already exits")
                flag = false
                break
            }
        }
        return flag
    }

    fun checkSId(sId: String): Boolean {
        var flag = true
        for (salesMan in database.salesManList) {
            if (salesMan.userId == sId) {
                println("salesman id already exits")
                flag = false
                break
            }
        }
        return flag
    }

    fun getItem(id: String): Item? {
        for (i in database.itemList) {
            if (id == i.itemId) {
                return i
            }
        }
        return null
    }

    val mangerList: Unit
        get() {
            for (m in database.managerList) {
                println("manger id :" + m.userId + " --manger name :" + m.name)
            }
        }
    val saleManList: Unit
        get() {
            for (sm in database.salesManList) {
                println("salesMan id :" + sm.userId + " --saleMan name :" + sm.name)
            }
        }
    val itemList: Unit
        get() {
            for (i in database.itemList) {
                println("item id :" + i.itemId + " --item name :" + i.itemName + " --item price :" + i.itemPrice + " --item quantity :" + i.quantity)
            }
        }

    fun assignManger(manger: Manger?, password: String) {
        if (password == "owner") {
            database.addManager(manger!!)
        } else {
            println("Access denied")
        }
    }

    fun assignSalesMan(salesMan: SalesMan?, password: String) {
        if (password == "owner") {
            database.addSaleMan(salesMan!!)
        } else {
            println("Access denied")
        }
    }

    fun addOwner(owner: Owner?) {
        database.owner = owner
    }

    val owner: Owner?
        get() = database.owner

    fun getLoginDetails(id: String, password: String, choice: Int): Boolean {
        if (choice == 2) {
            for (p in database.managerList) {
                val account = p.account
                if (id == account.id && password == account.password) {
                    return true
                }
            }
        } else if (choice == 3) {
            for (p in database.salesManList) {
                val account = p.account
                if (id == account.id && password == account.password) {
                    return true
                }
            }
        }
        return false
    }

    fun addItem(item: Item, idNo: String) {
        if (database.managerList.contains(getManger(idNo)) || idNo == "owner") {
            database.addItem(item)
            database.addSales(item)
            database.addPurchase(item, item.quantity)
        } else {
            println("Access denied")
        }
    }

    fun deleteItem(id: String, idNo: String) {
        if (database.managerList.contains(getManger(idNo)) || idNo == "owner") {
            val item: Item? = getItem(id)
            if (item != null) {
                database.removeItem(item)
            } else {
                println("item not found")
            }
        } else {
            println("access denied")
        }
    }

    fun updateItem(updateQuantity: Float, quantity: Float, item: Item, userType: UserType, type: String) {
        if (type == "sales") {
            if (userType == UserType.MANAGER || userType == UserType.SALESMAN || userType == UserType.OWNER) {
                val item1: Item = item
                item1.quantity = quantity
                val itemList = database.itemList
                itemList[itemList.indexOf(item)] = item1
                database.itemList = itemList
                val salesList = database.salesList
                var value = salesList[item]!!
                value += updateQuantity
                salesList[item] = value
                database.salesList = salesList
            } else {
                println("access denied")
            }
        } else if (type == "purchase") {
            if (userType == UserType.MANAGER || userType == UserType.OWNER) {
                val item1: Item = item
                item1.quantity = quantity
                val itemList = database.itemList
                itemList[itemList.indexOf(item)] = item1
                database.itemList = itemList
                val purchaseList = database.purchaseList
                var value = purchaseList[item]!!
                value += updateQuantity
                purchaseList[item] = value
                database.purchaseList = purchaseList
            } else {
                println("access denied")
            }
        }
    }
}