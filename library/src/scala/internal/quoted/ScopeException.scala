package scala.internal.quoted

/** Exception thrown when an Expr or Type is used ouside of the scope where it is valid */
private[scala] class ScopeException(msg: String) extends Exception(msg)