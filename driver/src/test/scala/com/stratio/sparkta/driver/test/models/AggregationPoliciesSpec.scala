/**
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.stratio.sparkta.driver.test.models

import com.stratio.sparkta.driver.models._
import com.stratio.sparkta.sdk.{DimensionType, JsoneyString}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import org.scalatest.{Matchers, WordSpecLike}

@RunWith(classOf[JUnitRunner])
class AggregationPoliciesSpec extends WordSpecLike
with MockitoSugar
with Matchers {

  "A AggregationPoliciesValidator should" should {
    "validate dimensions is required and has at least 1 element" in {

      val sparkStreamingWindow = 2000
      val checkpointInterval = 10000
      val checkpointAvailable = 60000
      val checkpointGranularity = "minute"
      val checkpointDir = "checkpoint"
      val checkpointDto =
        new CheckpointModel(checkpointDir, "", checkpointGranularity, checkpointInterval, checkpointAvailable)

      val configuration: Map[String, JsoneyString] =
        Map(("topics", new JsoneyString("zion2:1")), ("kafkaParams.group.id", new JsoneyString("kafka-pruebas")))
      val input = new PolicyElementModel("kafka-input", "KafkaInput", configuration)

      val cubeName = "cubeTest"
      val DimensionToCube = "dimension2"
      val cubeDto = new CubeModel(cubeName, Seq(new DimensionModel(
        DimensionToCube, "field1", DimensionType.IdentityName, DimensionType.DefaultDimensionClass, None)),
        Seq())

      val rawDataDto = new RawDataModel()

      val apd = new AggregationPoliciesModel(
        "policy-name",
        sparkStreamingWindow,
        rawDataDto,
        Seq(),
        Seq(cubeDto),
        Seq(input),
        Seq(mock[PolicyElementModel]),
        Seq(mock[FragmentElementModel]),
      checkpointDto)

      val test = AggregationPoliciesValidator.validateDto(apd)

      test._1 should equal(true)

      val sparkStreamingWindowBad = 20000
      val checkpointIntervalBad = 10000
      val checkpointDtoBad =
        new CheckpointModel(checkpointDir, "", checkpointGranularity, checkpointIntervalBad, checkpointAvailable)
      val apdBad = new AggregationPoliciesModel(
        "policy-name",
        sparkStreamingWindowBad,
        rawDataDto,
        Seq(),
        Seq(cubeDto),
        Seq(input),
        Seq(mock[PolicyElementModel]),
        Seq(mock[FragmentElementModel]),
        checkpointDtoBad)

      val test2 = AggregationPoliciesValidator.validateDto(apdBad)

      test2._1 should equal(false)


    }
  }
}