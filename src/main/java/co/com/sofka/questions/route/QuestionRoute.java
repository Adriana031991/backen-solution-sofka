package co.com.sofka.questions.route;

import co.com.sofka.questions.model.QuestionDTO;
import co.com.sofka.questions.usecase.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;


import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class QuestionRoute {
    @Bean
    public RouterFunction<ServerResponse> create(CreateUseCase createUseCase) {
        return route(POST("/create").and(accept(MediaType.APPLICATION_JSON)),
                request -> request.bodyToMono(QuestionDTO.class)
                        .flatMap(questionDTO -> createUseCase.apply(questionDTO)
                                .flatMap(result -> ServerResponse.ok()
                                        .contentType(MediaType.TEXT_PLAIN)
                                        .bodyValue(result))
                        )
        );
    }
    //adiciona dependencias
    @Bean
    public RouterFunction<ServerResponse> get(GetQuestionUseCase getQuestionUseCase){
        return route(GET("/get/{id}").and(accept(MediaType.APPLICATION_JSON)),
                request -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromPublisher(getQuestionUseCase.apply(
                        request.pathVariable("id")),
                        QuestionDTO.class))
        );
    }

    @Bean
    public RouterFunction<ServerResponse> delete(DeleteUseCase deleteUseCase){
        return route(DELETE("{id}").and(accept(MediaType.APPLICATION_JSON)),
                request -> ServerResponse.accepted()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromPublisher(deleteUseCase.apply(request.pathVariable("id")),
                        void.class))
        );
    }

    @Bean
    public RouterFunction<ServerResponse> getAll(ListUseCase listUseCase){
        return route(GET("all").and(accept(MediaType.APPLICATION_JSON)),
                request -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromPublisher(listUseCase.get(), QuestionDTO.class)
        ));

    }

    @Bean
    public RouterFunction<ServerResponse> ownerList(OwnerListUseCase ownerListUseCase){
        return route(GET("/owner/{userId}").and(accept(MediaType.APPLICATION_JSON)),
                request -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromPublisher(ownerListUseCase.apply(
                                request.pathVariable("userId")),
                                QuestionDTO.class)));

    }



}