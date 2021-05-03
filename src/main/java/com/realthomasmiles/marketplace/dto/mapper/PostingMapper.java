package com.realthomasmiles.marketplace.dto.mapper;

import com.realthomasmiles.marketplace.dto.model.marketplace.PostingDto;
import com.realthomasmiles.marketplace.model.marketplace.Posting;
import com.realthomasmiles.marketplace.util.DateUtils;

public class PostingMapper {
    public static PostingDto toPostingDto(Posting posting) {
        return new PostingDto()
                .setPosted(DateUtils.formattedDate(posting.getPosted()))
                .setArticle(posting.getArticle())
                .setAuthorId(posting.getAuthorId())
                .setDescription(posting.getDescription())
                .setCategoryId(posting.getCategoryId())
                .setIsActive(posting.getIsActive())
                .setLocationId(posting.getLocationId())
                .setPrice(posting.getPrice())
                .setName(posting.getName());
    }
}