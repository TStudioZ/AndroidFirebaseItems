package com.tstudioz.androidfirebaseitems.domain.mapper;

public interface IMapper<Source, Destination> {
    Source mapToSource(Destination destination);
    Destination mapToDestination(String key, Source source);
}
