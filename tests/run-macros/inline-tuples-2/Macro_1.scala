
import scala.quoted._

object Macros {

  def impl(tup: Expr[Tuple1[Int]]) (using QuoteContext): Expr[Int] = Expr(tup.unliftOrError._1)

  def impl2(tup: Expr[Tuple1[Tuple1[Int]]]) (using QuoteContext): Expr[Int] = impl(Expr(tup.unliftOrError._1))

}
