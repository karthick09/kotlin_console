import java.time.LocalDate

abstract class User(
    open val name: String,
    open val userId: String,
    open var email: String,
    open var phone: String,
    val account: Account,
    val userType: UserType
) {
    val dateOfJoin: LocalDate = LocalDate.of(2022, 1, 4)

    abstract fun sales()
}
