package model

// Petición de Autenticación (POST /users/auth)
data class emur_AuthRequest(
    val username: String,
    val password: String
)

// Petición de Creación de Sala (POST /rooms)
data class emur_CreateRoomRequest(
    val room: String,
    val capacity: Int
)

// Petición de Reserva de Sala (PUT /rooms/booking)
data class emur_BookingRequest(
    val room: String,
    val username: String
)

// Petición de Liberación de Sala (PUT /rooms/unbooking)
data class emur_UnbookingRequest(
    val room: String
)