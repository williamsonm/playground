package fs2pg

import cats.Monoid

final case class Stats(successCount: Int, errorCount: Int)

object Stats {
  implicit final val statsMonoid: Monoid[Stats] = new Monoid[Stats] {
    override def empty: Stats =
      Stats(successCount = 0, errorCount = 0)

    override def combine(x: Stats, y: Stats): Stats = Stats(
      successCount = x.successCount + y.successCount,
      errorCount = x.errorCount + y.errorCount
    )
  }
}
