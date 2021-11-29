import org.scalatest._
import flatspec._
import matchers._

class RleEncodingSpec extends AnyFlatSpec with should.Matchers {
  val rleEncoding = new RleEncoding

  "RleEncoding" should "encode given string XAABBCCDD" in {
    rleEncoding.encode("XAABBCCDD") shouldBe List(
      UncompressedBlock(1,List('X')),
      CompressedBlock(2,'A'),
      CompressedBlock(2,'B'),
      CompressedBlock(2,'C'),
      CompressedBlock(2,'D'))
  }

  it should "encode given string" in {
    rleEncoding.encode("AAAABBBCCXYZDDDDEEEFFFAAAAAABBBBBBBBBBBBBBBBBBBBBBBBBBBBBX") shouldBe List(
      CompressedBlock(4,'A'),
      CompressedBlock(3,'B'),
      CompressedBlock(2,'C'),
      UncompressedBlock(3,List('X', 'Y', 'Z')),
      CompressedBlock(4,'D'),
      CompressedBlock(3,'E'),
      CompressedBlock(3,'F'),
      CompressedBlock(6,'A'),
      CompressedBlock(29,'B'),
      UncompressedBlock(1,List('X'))
    )
  }
}