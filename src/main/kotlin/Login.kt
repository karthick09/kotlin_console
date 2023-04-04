class Login {
    fun login(id: String?, password: String?, choice: Int): Boolean {
        return DatabaseManager.getLoginDetails(id!!, password!!, choice)
    }
}