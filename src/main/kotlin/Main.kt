import DatabaseManager.addOwner
import DatabaseManager.getManger
import DatabaseManager.getSalesMan
import DatabaseManager.itemList
import DatabaseManager.mangerList
import DatabaseManager.owner
import DatabaseManager.saleManList
import java.util.*
import kotlin.system.exitProcess

object Main {
    private fun validateInteger(): Int {
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

    @JvmStatic
    fun main(args: Array<String>) {
        var user: User?
        val sc = Scanner(System.`in`)
        val ac3 = Account("abc", "123", AccountStatus.ACTIVE)
        val o = Owner("karthick", "m101", "karthickm@gmnvia.fv", "1234567890", ac3, UserType.OWNER, "owner")
        addOwner(o)
        do {
            var choice2: Int
            var flag = true
            println("1.owner login \n2.manger login \n3.salesman \n4.exit")
            val choice1: Int = validateInteger()
            if (choice1 == 4) {
                exitProcess(0)
            }
            val login = Login()
            println("enter the username")
            val username: String = sc.next()
            println("enter the password")
            val password: String = sc.next()
            when (choice1) {
                1 -> {
                    if (username == "abc" && password == "123") {
                        do {
                            user = owner
                            println(
                                """
                                    1.Add manager 
                                    2.Add salesperson 
                                    3.showList 
                                    4.salary for employee
                                    5.turnover
                                    6.Add item 
                                    7.Delete item 
                                    8.sales 
                                    9.purchase
                                    10.exit
                                    """.trimIndent()
                            )
                            choice2 = validateInteger()
                            when (choice2) {
                                1 -> user!!.addManager()
                                2 -> user!!.addSalesPerson()
                                3 -> {
                                    println("1.manger list \n2.salesman list \n3.item list")
                                    when (validateInteger()) {
                                        1 -> mangerList
                                        2 -> saleManList
                                        3 -> itemList
                                        else -> println("invalid choice")
                                    }
                                }

                                4 -> user!!.salary()
                                5 -> {
                                    println("1.total sales\n2.total purchase")
                                    val choice = validateInteger()
                                    user!!.turnover(choice)
                                }

                                6 -> user!!.addItem(user.password)
                                7 -> user!!.deleteItem(user.password)
                                8 -> user!!.sales()
                                9 -> {
                                    println("1.To update item \n2.new item")
                                    when (validateInteger()) {
                                        1 -> {
                                            user!!.purchase()
                                        }
                                        2 -> {
                                            user!!.addItem(user.password)
                                        }
                                        else -> {
                                            println("invalid choice")
                                        }
                                    }
                                }

                                10 -> {
                                    flag = false
                                    println("Exited successfully")
                                }

                                else -> println("invalid choice")
                            }
                        } while (flag)
                    } else {
                        println("invalid username and password")
                    }
                }

                2 -> {
                    if (login.login(username, password, choice1)) {
                        user = getManger(username)
                        if (user == null) {
                            println("manager not found")
                            break
                        }
                        do {
                            println("1.showItemList \n2.Add item \n3.Delete item \n4.sales \n5.purchase\n6.exit")
                            choice2 = validateInteger()
                            when (choice2) {
                                1 -> user.showItemList()
                                2 -> user.addItem(username)
                                3 -> user.deleteItem(username)
                                4 -> user.sales()
                                5 -> {
                                    println("1.To update item \n2.new item")
                                    when (validateInteger()) {
                                        1 -> {
                                            user.purchase()
                                        }
                                        2 -> {
                                            user.addItem(username)
                                        }
                                        else -> {
                                            println("invalid choice")
                                        }
                                    }
                                }

                                6 -> {
                                    println("Exited successfully")
                                    flag = false
                                }

                                else -> println("invalid choice")
                            }
                        } while (flag)
                    } else {
                        println("invalid username or password")
                    }
                }

                3 -> {
                    if (login.login(username, password, choice1)) {
                        user = getSalesMan(username)
                        if (user == null) {
                            println("salesman not found ")
                            break
                        }
                        do {
                            println("1.view list \n2.sales\n3.exit")
                            choice2 = validateInteger()
                            when (choice2) {
                                1 -> user.showItemList()
                                2 -> user.sales()
                                3 -> {
                                    println("Exited successfully")
                                    flag = false
                                }

                                else -> println("invalid choice")
                            }
                        } while (flag)
                    } else {
                        println("invalid username or password")
                    }
                }

                else -> println("invalid choice")
            }
        } while (true)
    }
}