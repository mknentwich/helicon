package at.markusnentwich.helicon.services

const val OK = "OK"
const val BAD_REQUEST = "Bad Request. This may happen on an invalid authorization header."
const val UNAUTHORIZED = "Unauthorized. This happens when no authorization happened."
const val FORBIDDEN = "Forbidden. This happens when the authorized user has not enough permissions."
const val UNPROCESSABLE_ENTITY = "Unprocessable Entity. There is an error in the request body."
const val CONFLICT = "Conflict. a resource with that id already exists."