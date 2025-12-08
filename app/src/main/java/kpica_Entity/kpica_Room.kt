package kpica_Entity

import java.time.LocalDate

class kpica_Room {
    private var room: String = ""
    private var capacity: Int = 0
    private var is_busy: Boolean = false
    private var user: String = ""
    private var date: LocalDate? = null

    constructor()

    var Room: String
        get() = this.room
        set(value) {
            this.room = value
        }

    var Capacity: Int
        get() = this.capacity
        set(value) {
            this.capacity = value
        }

    var Is_Busy: Boolean
        get() = this.is_busy
        set(value) {
            this.is_busy = value
        }

    var User: String
        get() = this.user
        set(value) {
            this.user = value
        }

    var Date: LocalDate?
        get() = this.date
        set(value) {
            this.date = value
        }
}