package model

class ken_User {

    private var username: String = ""
    private var password: String = ""
    private var name: String = ""
    private var isActive: Boolean = false
    private var lastname: String = ""
    private var email: String = ""

    // Constructor vacío
    constructor()

    // Constructor con parámetros
    constructor(
        username: String,
        password: String,
        name: String,
        isActive: Boolean,
        lastname: String,
        email: String
    ) {
        this.username = username
        this.password = password
        this.name = name
        this.isActive = isActive
        this.lastname = lastname
        this.email = email
    }

    // Getters y Setters
    fun getUsername(): String {
        return username
    }

    fun setUsername(username: String) {
        this.username = username
    }

    fun getPassword(): String {
        return password
    }

    fun setPassword(password: String) {
        this.password = password
    }

    fun getName(): String {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getIsActive(): Boolean {
        return isActive
    }

    fun setIsActive(isActive: Boolean) {
        this.isActive = isActive
    }

    fun getLastname(): String {
        return lastname
    }

    fun setLastname(lastname: String) {
        this.lastname = lastname
    }

    fun getEmail(): String {
        return email
    }

    fun setEmail(email: String) {
        this.email = email
    }
}