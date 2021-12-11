import org.scalatest.flatspec.AnyFlatSpec

class RleEncodingPerfComp extends AnyFlatSpec {

  val rleEncoding = new RleEncoding
  val rleEncodingMut = new RleEncodingMuttable
  val rleEncodingOptim = new RleEncodingOptim


  val shortExample = Seq("XAABBCCD", "ABCDE")
  val longExample = "AAAABBBCCXYZDDDDEEEFFFAAAAAABBBBBBBBBBBBBBBBBBBBBBBBBBBBBX" * 100000

//  shortExample.map(rleEncoding.encode)
//  shortExample.map(rleEncodingMut.encode)
  shortExample.map(rleEncodingOptim.encode)

  measure("Optimize vesion", rleEncodingOptim.encode(longExample))
//  measure("Muttable vesion", rleEncodingMut.encode(longExample))
//  measure("Default vesion", rleEncoding.encode(longExample))

  def measure(className:String, f: => Iterable[_]): Iterable[_] ={
    println(s"start measuring: $className")
    val start = System.nanoTime()
    val r = f
    val finish = System.nanoTime() - start
    println(s"result: ${finish /(1000 * 1000)} ms")
    r
  }
}
