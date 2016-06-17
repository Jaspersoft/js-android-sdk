package com.jaspersoft.android.sdk.widget.report.v2;

/**
 * @author Tom Koptel
 * @since 2.6
 */
public class ReportQuery {
    private final int page;
    private final String anchor;

    private ReportQuery(Builder builder) {
        this.page = builder.page;
        this.anchor = builder.anchor;
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    public int getPage() {
        return page;
    }

    public String getAnchor() {
        return anchor;
    }

    @Override
    public String toString() {
        return "ReportQuery{" +
                "page='" + page + '\'' +
                ", anchor='" + anchor + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReportQuery query = (ReportQuery) o;

        if (page != query.page) return false;
        return anchor != null ? anchor.equals(query.anchor) : query.anchor == null;

    }

    @Override
    public int hashCode() {
        int result = page;
        result = 31 * result + (anchor != null ? anchor.hashCode() : 0);
        return result;
    }

    public static class Builder {
        private int page = Integer.MIN_VALUE;
        private String anchor;

        public Builder() {}

        private Builder(ReportQuery query) {
            this.page = query.page;
            this.anchor = query.anchor;
        }

        public Builder page(int page) {
            this.page = page;
            return this;
        }

        public Builder anchor(String anchor) {
            this.anchor = anchor;
            return this;
        }

        public ReportQuery build() {
            if (page == Integer.MIN_VALUE && anchor == null) {
                throw new IllegalArgumentException("There should be either anchor, page or both.");
            }
            return new ReportQuery(this);
        }
    }
}
