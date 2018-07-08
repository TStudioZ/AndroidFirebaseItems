package com.tstudioz.androidfirebaseitems.data;

public interface IMapper<Source, Destination> {
    Source mapToSource(Destination destination);
    Destination mapToDestination(Source source);
}
