package com.software.service;

import com.software.model.Tag;
import com.software.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;

    public void addTag(Tag tag) {
        tagRepository.save(tag);
    }
}
