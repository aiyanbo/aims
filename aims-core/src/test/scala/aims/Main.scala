package aims

/**
 * Component:
 * Description:
 * Date: 2014/12/30
 * @author Andy Ai
 */
object Main extends App {
  val aims = Aims("hello-aims")
  aims.registerService(new CouponOperationResource)
  aims.startup()
  aims.awaitShutdown
}
