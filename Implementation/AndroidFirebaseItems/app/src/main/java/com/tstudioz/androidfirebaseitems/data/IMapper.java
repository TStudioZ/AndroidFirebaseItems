package com.tstudioz.androidfirebaseitems.data;

public interface IMapper<From, To> {
    To map(From from);
}
