package it.mdtorelli.cashflows.model

import it.mdtorelli.cashflows.model.Implicits._
import it.mdtorelli.cashflows.util.BaseSpec

final class ModelSpec extends BaseSpec {
  behavior of "All models"

  it should "not be accessible using the constructor" in {
    "new Id(???)" shouldNot compile
    "new Decimal(???)" shouldNot compile
    "new Schedule(???, ???, ???, ???)" shouldNot compile
    "new CashFlow(???, ???, ???, ???)" shouldNot compile
    "new APR(???)" shouldNot compile
    "new IRR(???)" shouldNot compile
    "new ComputationResult(???, ???)" shouldNot compile
  }

  behavior of "Decimal"

  it should "support configurable scale - precision 1" in new Fixture {
    val scale = 1
    override protected final val lowerRoundCases = Seq(55.33466, 55.33511, 55.33611, 55.34466, 55.34511, 55.34611)
    override protected final val upperRoundCases = Seq(55.35466, 55.35511, 55.35611, 55.36466, 55.36511, 55.36611)
    override protected final val expectedLowerRoundResults = Seq(55.3)
    override protected final val expectedUpperRoundResults = Seq(55.4)

    lowerRoundCases.map(_.toDecimal(scale)).foreach { x =>
      expectedLowerRoundResults should contain(x.value)
    }
    upperRoundCases.map(_.toDecimal(scale)).foreach { x =>
      expectedUpperRoundResults should contain(x.value)
    }
  }

  it should "support configurable scale - precision 2" in new Fixture {
    final val scale = 2
    override protected final val lowerRoundCases = Seq(55.33466, 55.33511, 55.33611, 55.34466, 55.34511, 55.34611)
    override protected final val upperRoundCases = Seq(55.35466, 55.35511, 55.35611, 55.36466, 55.36511, 55.36611)
    override protected final val expectedLowerRoundResults = Seq(55.33, 55.34, 55.35)
    override protected final val expectedUpperRoundResults = Seq(55.35, 55.36, 55.37)

    lowerRoundCases.map(_.toDecimal(scale)).foreach { x =>
      expectedLowerRoundResults should contain(x.value)
    }
    upperRoundCases.map(_.toDecimal(scale)).foreach { x =>
      expectedUpperRoundResults should contain(x.value)
    }
  }

  behavior of "APR"

  it should "transform APR in a Decimal having scale 1" in new Fixture {
    override protected final val lowerRoundCases = Seq(55.33466, 55.33511, 55.33611, 55.34466, 55.34511, 55.34611)
    override protected final val upperRoundCases = Seq(55.35466, 55.35511, 55.35611, 55.36466, 55.36511, 55.36611)
    override protected final val expectedLowerRoundResults = Seq(55.3)
    override protected final val expectedUpperRoundResults = Seq(55.4)

    lowerRoundCases.map(_.toDecimal.toAPR.scaled).foreach { result =>
      expectedLowerRoundResults should contain(result.value.value)
    }
    upperRoundCases.map(_.toDecimal.toAPR.scaled).foreach { result =>
      expectedUpperRoundResults should contain(result.value.value)
    }
  }

  behavior of "IRR"

  it should "transform IRR in a Decimal having scale 10" in new Fixture {
    override protected final val lowerRoundCases =
      Seq(0.02340087833411, 0.02340087833511, 0.02340087833611, 0.02340087834411, 0.02340087834511, 0.02340087834611)
    override protected final val upperRoundCases =
      Seq(0.02340087835411, 0.02340087835511, 0.02340087835611, 0.02340087836411, 0.02340087836511, 0.02340087836611)
    override protected final val expectedLowerRoundResults = Seq(0.0234008783)
    override protected final val expectedUpperRoundResults = Seq(0.0234008784)

    lowerRoundCases.map(_.toDecimal.toIRR.scaled).foreach { result =>
      expectedLowerRoundResults should contain(result.value.value)
    }
    upperRoundCases.map(_.toDecimal.toIRR.scaled).foreach { result =>
      expectedUpperRoundResults should contain(result.value.value)
    }
  }

  private sealed trait Fixture {
    protected val expectedLowerRoundResults: Seq[BigDecimal]
    protected val expectedUpperRoundResults: Seq[BigDecimal]

    protected val lowerRoundCases: Seq[BigDecimal]
    protected val upperRoundCases: Seq[BigDecimal]
  }
}
