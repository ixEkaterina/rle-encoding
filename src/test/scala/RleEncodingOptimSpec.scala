import org.scalatest.flatspec._
import org.scalatest.matchers._

class RleEncodingOptimSpec extends AnyFlatSpec with should.Matchers {
  val rleEncoding = new RleEncodingOptim

  "RleEncoding" should "encode given string XAABBCCDD" in {
    rleEncoding.encode("XAABBCCDD") shouldBe List(
      UncompressedBlockO(1,List('X')),
      CompressedBlockO(2,'A'),
      CompressedBlockO(2,'B'),
      CompressedBlockO(2,'C'),
      CompressedBlockO(2,'D'))
  }

  it should "encode given string" in {
    rleEncoding.encode("AAAABBBCCXYZDDDDEEEFFFAAAAAABBBBBBBBBBBBBBBBBBBBBBBBBBBBBX") shouldBe List(
      CompressedBlockO(4,'A'),
      CompressedBlockO(3,'B'),
      CompressedBlockO(2,'C'),
      UncompressedBlockO(3,List('X', 'Y', 'Z')),
      CompressedBlockO(4,'D'),
      CompressedBlockO(3,'E'),
      CompressedBlockO(3,'F'),
      CompressedBlockO(6,'A'),
      CompressedBlockO(29,'B'),
      UncompressedBlockO(1,List('X'))
    )
  }
}