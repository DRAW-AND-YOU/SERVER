package com.drawandyou.drawandyou_server.domain.article.presentation.dto.response;

import java.util.List;

public record PopularArticleListResponse(
        List<PopularArticleResponse> popularArticles
) {
}
