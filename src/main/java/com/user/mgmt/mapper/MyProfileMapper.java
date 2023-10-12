package com.user.mgmt.mapper;

import com.user.mgmt.entity.AddressEntity;
import com.user.mgmt.entity.UserEntity;
import com.user.mgmt.request.MyProfileRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MyProfileMapper {

    MyProfileMapper INSTANCE = Mappers.getMapper(MyProfileMapper.class);

    @Mapping( target = "UserEntity.email", ignore = true )
    void updateUserEntity(MyProfileRequest request, @MappingTarget UserEntity userEntity);

    @Mapping(target = "AddressEntity.addressLine1", ignore = true)
    AddressEntity updateAddressEntity(MyProfileRequest request, @MappingTarget AddressEntity addressEntity);

}
