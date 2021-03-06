package com.codependent.reactivegames.route

import com.codependent.reactivegames.dto.Game
import com.codependent.reactivegames.repository.GamesRepository
import com.codependent.reactivegames.web.handler.ApiHandlers
import com.codependent.reactivegames.web.route.ApiRoutes
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.ApplicationContext
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux


@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [ApiRoutes::class, ApiHandlers::class])
@WebFluxTest
class ApiRoutesTests(@Autowired private val context: ApplicationContext) {

    @MockBean
    private lateinit var gamesRepository: GamesRepository

    private lateinit var testWebClient: WebTestClient

    @BeforeEach
    fun setUp() {
        testWebClient = WebTestClient.bindToApplicationContext(context).build()
    }

    @Test
    fun shouldGetGames() {
        val games = Flux.just(Game("Maniac Mansion"), Game("The Secret of Monkey Island"))
        `when`(gamesRepository.findAll()).thenReturn(games)
        testWebClient.get().uri("/api/v1/games", 2).accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk
                .expectBody(object : ParameterizedTypeReference<List<Game>>() {})
        //.isEqualTo(customerMap.findAll("Peter"))
    }

}