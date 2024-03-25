package ru.app.util;

import java.util.ArrayList;
import java.util.List;

public class ValidParameters {

    private static final List<String> listRequestColumnParameters;
    private static final List<String> listRequestSortParameters;

    static {
        listRequestColumnParameters = new ArrayList<>(List.of("book",
                                                              "author",
                                                              "numPages",
                                                              "publicationDate",
                                                              "rating",
                                                              "numberOfVoters"));

        listRequestSortParameters = new ArrayList<>(List.of("ASC",
                                                            "DESC"));
    }

    public static List<String> getListRequestColumnParameters() {
        return List.copyOf(listRequestColumnParameters);
    }

    public static List<String> getListRequestSortParameters() {
        return List.copyOf(listRequestSortParameters);
    }

    public static boolean isValidColumnParameter(String columnParameter) {
        return listRequestColumnParameters.contains(columnParameter);
    }

    public static boolean isValidSortParameter(String sortParameter) {
        return listRequestSortParameters.contains(sortParameter);
    }
}
