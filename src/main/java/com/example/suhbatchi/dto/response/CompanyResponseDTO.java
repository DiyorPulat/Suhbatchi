package com.example.suhbatchi.dto.response;


import lombok.Data;
import java.util.List;

@Data
public class CompanyResponseDTO {
    private Company company;
    private CompanyBillingAddress companyBillingAddress;
    private CompanyExtraInfo companyExtraInfo;
    private Person director;
    private Address directorAddress;
    private Contact directorContact;
    private CompanyContact companyContact;
    private List<Founder> founders;

    @Data
    public static class Company {
        private String tin;
        private String created;
        private String updated;
        private String name;
        private String shortName;
        private Integer opf;
        private Integer kfs;
        private String oked;
        private Integer soato;
        private String soogu;
        private String sooguRegistrator;
        private String registrationDate;
        private String registrationNumber;
        private String reregistrationDate;
        private Integer status;
        private Long vatNumber;
        private Integer taxpayerType;
        private Integer businessType;
        private Long businessFund;
        private Integer businessStructure;
        private OpfDetail opfDetail;
        private SooguDetail sooguDetail;
        private StatusDetail statusDetail;
        private BusinessStructureDetail businessStructureDetail;
        private OkedDetail okedDetail;
        private Integer villageCode;
        private String villageName;
        private String streetName;
        private String addressId;
        private String vatBeginDate;
        private String vatFromDate;
        private String vatStatus;
        private BusinessTypeDetail businessTypeDetail;
        private List<ActivityType> activityTypes;
        private Boolean cottonCluster;

        @Data
        public static class OpfDetail {
            private String code;
            private String name_ru;
            private String name_uz_latn;
        }

        @Data
        public static class SooguDetail {
            private String code;
            private String name;
            private String name_ru;
            private String name_uz_cyrl;
            private String name_uz_latn;
        }

        @Data
        public static class StatusDetail {
            private String code;
            private String name;
            private String name_ru;
            private String name_uz_cyrl;
            private String name_uz_latn;
            private String group;
        }

        @Data
        public static class BusinessStructureDetail {
            private String code;
            private String name_ru;
            private String name_uz_cyrl;
            private String name_uz_latn;
        }

        @Data
        public static class OkedDetail {
            private String code;
            private String name;
            private String name_ru;
            private String name_uz_cyrl;
            private String name_uz_latn;
            private String section;
            private String pkm275;
            private Integer employee_limit_mf;
            private Integer employee_limit_lf;
        }

        @Data
        public static class BusinessTypeDetail {
            private String code;
            private String name;
            private String name_ru;
            private String name_uz_cyrl;
            private String name_uz_latn;
        }

        @Data
        public static class ActivityType {
            private String code;
            private String parentCode;
            private String name;
            private String name_ru;
            private String name_uz_cyrl;
            private String name_uz_latn;
            private Integer level_id;
            private String id;
        }
    }

    @Data
    public static class CompanyBillingAddress {
        private String id;
        private Integer countryCode;
        private Region region;
        private District district;
        private Integer sectorCode;
        private String streetName;
        private Integer soatoCode;
    }

    @Data
    public static class CompanyExtraInfo {
        private Integer avgNumberEmployees;
    }

    @Data
    public static class Person {
        private String lastName;
        private String firstName;
        private String middleName;
        private Integer gender;
        private String nationality;
        private String citizenship;
        private String passportSeries;
        private String passportNumber;
        private String pinfl;
        private String tin;
        private String birthDate;
    }

    @Data
    public static class Address {
        private String id;
        private Integer countryCode;
        private Region region;
        private District district;
        private Integer sectorCode;
        private String village;
        private String streetName;
        private Integer soatoCode;
    }

    @Data
    public static class Region {
        private Integer code;
        private String name;
        private String name_ru;
        private String name_uz_cyrl;
        private String name_uz_latn;
    }

    @Data
    public static class District {
        private Integer code;
        private String name;
        private String name_ru;
        private String name_uz_cyrl;
        private String name_uz_latn;
        private Integer regionCode;
        private Integer districtId;
    }

    @Data
    public static class Contact {
        private String phone;
        private String email;
    }

    @Data
    public static class CompanyContact {
        private String phone;
        private String email;
    }

    @Data
    public static class Founder {
        private String tin;
        private String name;
        private String type;
        private Integer sharePersent;
    }
}

