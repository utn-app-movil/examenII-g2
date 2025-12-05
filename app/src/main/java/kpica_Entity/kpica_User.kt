package kpica_Entity

class kpica_User {
    private var username: String = ""
    private var password: String? = ""
    private var name: String = ""
    private var lastname: String = ""
    private var email: String = ""
    private var isActive: Boolean? = true

    constructor()

    var Username: String
        get() = this.username
        set(value) {
            this.username = value
        }

    var Password: String?
        get() = this.password
        set(value) {
            this.password = value
        }

    var Name: String
        get() = this.name
        set(value) {
            this.name = value
        }

    var LastName: String
        get() = this.lastname
        set(value) {
            this.lastname = value
        }

    var Email: String
        get() = this.email
        set(value) {
            this.email = value
        }

    var IsActive: Boolean?
        get() = this.isActive
        set(value) {
            this.isActive = value
        }
}