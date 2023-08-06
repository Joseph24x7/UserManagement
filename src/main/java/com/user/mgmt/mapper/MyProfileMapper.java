package com.user.mgmt.mapper;

import com.user.mgmt.model.UserEntity;
import com.user.mgmt.request.MyProfileRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MyProfileMapper {

    MyProfileMapper INSTANCE = Mappers.getMapper(MyProfileMapper.class);

    void updateUserEntity(MyProfileRequest request, @MappingTarget UserEntity userEntity);
}
