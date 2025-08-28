package com.github.gianlucafattarsi.liberapi.context.library.controller.impl;

import com.github.gianlucafattarsi.liberapi.context.library.controller.SeriesApi;
import com.github.gianlucafattarsi.liberapi.context.library.dto.SerieDTO;
import com.github.gianlucafattarsi.liberapi.context.library.service.SeriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SeriesController implements SeriesApi {

    private final SeriesService seriesService;

    @Override
    public SerieDTO loadRandomSerie() {
        return seriesService.loadRandomSerie();
    }
}
