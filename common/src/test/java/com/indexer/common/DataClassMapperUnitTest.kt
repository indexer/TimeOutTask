package com.indexer.common

import com.indexer.common.mapper.DataClassMapper
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class DataClassMapperUnitTest {

  @Test
  fun `test mapping foo with default param in target`() {
    data class FooIn(val name: String)
    data class FooOut(
      val name: String,
      val age: Int
    )

    val fooWithTargetParamsMapper = DataClassMapper<FooIn, FooOut>()
      .targetParameterSupplier("age") { 18 }
    assertEquals(fooWithTargetParamsMapper(FooIn("yemon")), FooOut("yemon", 18))
  }

  @Test
  fun `simple mapping foo`() {
    data class FooIn(val name: String?)
    data class FooOut(val name: String)
    val fooMapper = DataClassMapper<FooIn, FooOut>()
    assertEquals(fooMapper(FooIn("kermit")),FooOut("kermit"))
  }

  @Test
  fun `test mapping foo with default param in target for null`() {
    data class FooIn(val name: String?)
    data class FooOut(
      val name: String,
      val age: Int
    )
    val fooWithTargetParamsMapper = DataClassMapper<FooIn, FooOut>()
      .targetParameterSupplier("name"){"Default Name"}
      .targetParameterSupplier("age") { 18 }
    assertEquals(fooWithTargetParamsMapper(FooIn(null)), FooOut("Default Name", 18))
  }
}