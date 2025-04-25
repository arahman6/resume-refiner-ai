package edu.miu.cs.cs489.resumerefinerai.service;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OpenAIService {

    private final OpenAIClient client;

    public OpenAIService(@Value("${openai.api.key}") String apiKey) {
        this.client = OpenAIOkHttpClient.builder()
                .apiKey(apiKey)
                .build();
    }

    public String getUpdatedSection(String sectionName, String sectionContent, String jobDescription) {
        String prompt = "You are a resume writing expert. Rewrite ONLY the LaTeX content of the resume section '" + sectionName +
                "' based on the job description below. Return ONLY LaTeX code between \\begin{} and \\end{}, no explanation, no Markdown, no notes.\n\n" +
                "Job Description:\n" + jobDescription + "\n\nCurrent LaTeX Section:\n" + sectionContent;


        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .addUserMessage(prompt)
                .model(ChatModel.GPT_4_1)
                .build();

        ChatCompletion completion = client.chat().completions().create(params);
        return completion.choices().get(0).message().content().orElse("%% Error: no content received");
    }
}
