package com.example.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.IntStream;

/**
 * ページネーション計算用ユーティリティクラス
 */
public class PaginationUtil {
    private PaginationUtil() {
    }

    /**
     * 指定されたページ番号および1ページあたりの表示件数から、
     * ページネーションにおける開始インデックス（オフセット）を計算
     *
     * @param page 現在のページ番号（1以上の整数である）
     * @param displayNumber 1ページあたりの表示件数
     * @return ページネーションにおける開始インデックス
     */
    public static int calculateOffset(int page, int displayNumber) {
        return (page - 1) * displayNumber;
    }

    /**
     * 総件数および1ページあたりの表示件数から、必要な総ページ数を算出する。
     *
     * @param totalCount 総件数
     * @param displayNumber 1ページあたりの表示件数
     * @return 総ページ数
     */
    public static int calculateTotalPage(int totalCount, int displayNumber) {
        BigDecimal total = BigDecimal.valueOf(totalCount);
        BigDecimal display = BigDecimal.valueOf(displayNumber);

        return total.divide(display, 0, RoundingMode.CEILING).intValue();
    }
    
    public static List<Integer> createPageNumbers(int currentPage, int totalPage, int radius){
        int from = Math.max(1, currentPage - radius);
        int to = Math.min(totalPage, currentPage + radius);
        return IntStream.rangeClosed(from, to).boxed().toList();
    }
}
