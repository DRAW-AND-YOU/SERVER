package com.drawandyou.drawandyou_server.domain.article.presentation.dto.response;

import com.drawandyou.drawandyou_server.global.common.response.ScrollInfo;

import java.util.List;

public record ArticleScrollResponse(
        List<ArticleResponse> articles,
        ScrollInfo scrollInfo
) {
    public static ArticleScrollResponse of(List<ArticleResponse> articles, Long requestedLimit) {
        // 요청한 limit보다 많이 조회했는지 확인한다.
        boolean hasNext = articles.size() > requestedLimit;

        // hasNext가 true면 마지막 요소는 제외하자.
        List<ArticleResponse> content = hasNext ? articles.subList(0, requestedLimit.intValue()) : articles;

        ScrollInfo scrollInfo = content.isEmpty() ?
                ScrollInfo.empty() :
                ScrollInfo.of(content.get(content.size() - 1).createdAt(), content.get(content.size() - 1).articleId(), hasNext, content.size());

        return new ArticleScrollResponse(content, scrollInfo);
    }
}