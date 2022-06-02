package com.example.goldenequatorassignment.state

enum class Status {
    LOADING,
    COMPLETED,
    ERROR,
}

class ConnectionState (val status: Status, val message: String) {

    companion object{

        val LOADING: ConnectionState = ConnectionState(Status.LOADING, "Loading")
        val COMPLETED: ConnectionState = ConnectionState(Status.COMPLETED, "Success")
        val ERROR: ConnectionState = ConnectionState(Status.ERROR, "Error while loading the data")

        val ENDOFLIST : ConnectionState = ConnectionState(Status.ERROR, "This is End of Movies List")

    }

}