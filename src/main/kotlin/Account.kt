class Account(val id: String, var password: String, status: AccountStatus) {
    private var status: AccountStatus

    init {
        this.status = status
    }

    fun setStatus(status: AccountStatus) {
        this.status = status
    }
}