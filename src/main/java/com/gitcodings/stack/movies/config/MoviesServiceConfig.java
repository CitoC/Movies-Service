package com.gitcodings.stack.movies.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "movie")
public class MoviesServiceConfig
{
}
