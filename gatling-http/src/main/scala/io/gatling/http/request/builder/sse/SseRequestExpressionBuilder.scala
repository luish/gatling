/*
 * Copyright 2011-2023 GatlingCorp (https://gatling.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.gatling.http.request.builder.sse

import io.gatling.core.config.GatlingConfiguration
import io.gatling.http.cache.HttpCaches
import io.gatling.http.client.{ RequestBuilder => ClientRequestBuilder }
import io.gatling.http.protocol.HttpProtocol
import io.gatling.http.request.builder.{ CommonAttributes, HttpAttributes, HttpRequestExpressionBuilder }

class SseRequestExpressionBuilder(
    commonAttributes: CommonAttributes,
    httpCaches: HttpCaches,
    httpProtocol: HttpProtocol,
    configuration: GatlingConfiguration
) extends HttpRequestExpressionBuilder(commonAttributes, HttpAttributes.Empty, httpCaches, httpProtocol, configuration) {
  // disable request timeout for SSE
  override protected def configureRequestTimeout(requestBuilder: ClientRequestBuilder): Unit =
    requestBuilder.setRequestTimeout(-1)
}
