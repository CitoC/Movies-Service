package com.gitcodings.stack.movies;

import com.gitcodings.stack.movies.config.MoviesServiceConfig;
import com.gitcodings.stack.core.spring.SecuredStackService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@SecuredStackService
@EnableConfigurationProperties({
    MoviesServiceConfig.class
})
public class MoviesService
{
    public static void main(String[] args)
    {
        SpringApplication.run(MoviesService.class, args);
    }
}
