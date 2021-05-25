package co.com.sofka.questions.usecase;

import co.com.sofka.questions.MapperUtils;
import co.com.sofka.questions.model.AnswerDTO;
import co.com.sofka.questions.model.QuestionDTO;
import co.com.sofka.questions.reposioties.AnswerRepository;
import co.com.sofka.questions.usecase.interfaces.SaveAnswer;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;

import java.util.Objects;
@Service
@Validated
public class AddAnswerUseCase implements SaveAnswer {
    private final AnswerRepository answerRepository;
    private final MapperUtils mapperUtils;
    private final GetQuestionUseCase getQuestionUseCase;

    public AddAnswerUseCase(AnswerRepository answerRepository, MapperUtils mapperUtils, GetQuestionUseCase getQuestionUseCase) {
        this.answerRepository = answerRepository;
        this.mapperUtils = mapperUtils;
        this.getQuestionUseCase = getQuestionUseCase;
    }

    @Override
    public Mono<QuestionDTO> apply(AnswerDTO answerDTO) {
        Objects.requireNonNull(answerDTO.getQuestionId(),"es necesario el Id de la pregunta");
        return getQuestionUseCase.apply(answerDTO.getQuestionId())
                .flatMap(question -> answerRepository.save(mapperUtils.mapperToAnswer().apply(answerDTO))
                .map(answer -> {
                    question.getAnswers().add(answerDTO);
                    return question;
                }));
    }
}
