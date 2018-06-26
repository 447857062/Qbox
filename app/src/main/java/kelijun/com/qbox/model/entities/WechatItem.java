package kelijun.com.qbox.model.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${kelijun} on 2018/6/26.
 */

public class WechatItem implements Parcelable {
    private String msg;
    private ResultBean result;
    private String retCode;

    protected WechatItem(Parcel in) {
        msg = in.readString();
        result = in.readParcelable(ResultBean.class.getClassLoader());
        retCode = in.readString();
    }

    public static final Creator<WechatItem> CREATOR = new Creator<WechatItem>() {
        @Override
        public WechatItem createFromParcel(Parcel in) {
            return new WechatItem(in);
        }

        @Override
        public WechatItem[] newArray(int size) {
            return new WechatItem[size];
        }
    };

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(msg);
        dest.writeParcelable(result, flags);
        dest.writeString(retCode);
    }

    public static class ResultBean implements Parcelable {
        private int curPage;
        private int total;
        private List<ListBean> list;

        public int getCurPage() {
            return curPage;
        }

        public void setCurPage(int curPage) {
            this.curPage = curPage;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        protected ResultBean(Parcel in) {
            this.curPage = in.readInt();
            this.total = in.readInt();
            this.list = new ArrayList<ListBean>();
            in.readList(this.list, ListBean.class.getClassLoader());
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.curPage);
            dest.writeInt(this.total);
            dest.writeList(this.list);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<ResultBean> CREATOR = new Creator<ResultBean>() {
            @Override
            public ResultBean createFromParcel(Parcel in) {
                return new ResultBean(in);
            }

            @Override
            public ResultBean[] newArray(int size) {
                return new ResultBean[size];
            }
        };

        public static class ListBean implements Parcelable, MultiItemEntity {
            public static final int STYLE_BIG = 1;
            public static final int STYLE_SMALL = 0;
            public static final int STYLE_SMALL_SPAN_SIZE = 1;
            public static final int STYLE_BIG_SPAN_SIZE = 2;
            public ListBean() {
            }
            private String cid;
            private String hitCount;
            private String id;
            private String pubTime;
            private String sourceUrl;
            private String subTitle;
            private String thumbnails;
            private String title;


            private int itemType=0;
            private int spanSize=1;

            public String getCid() {
                return cid;
            }

            public void setCid(String cid) {
                this.cid = cid;
            }

            public String getHitCount() {
                return hitCount;
            }

            public void setHitCount(String hitCount) {
                this.hitCount = hitCount;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getPubTime() {
                return pubTime;
            }

            public void setPubTime(String pubTime) {
                this.pubTime = pubTime;
            }

            public String getSourceUrl() {
                return sourceUrl;
            }

            public void setSourceUrl(String sourceUrl) {
                this.sourceUrl = sourceUrl;
            }

            public String getSubTitle() {
                return subTitle;
            }

            public void setSubTitle(String subTitle) {
                this.subTitle = subTitle;
            }

            public String getThumbnails() {
                if ((!TextUtils.isEmpty(thumbnails)) && thumbnails.contains("$")) {
                    for (String string : thumbnails.split("\\$", 3)) {
                        if (!TextUtils.isEmpty(string)) {
                            return string;
                        }
                    }
                }
                return thumbnails;
            }

            public void setThumbnails(String thumbnails) {
                this.thumbnails = thumbnails;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public void setItemType(int itemType) {
                if (itemType == 1 || itemType == 0) {
                    this.itemType = itemType;
                }else{
                    this.itemType = 0;
                }

            }

            public int getSpanSize() {

                return spanSize;
            }

            public void setSpanSize(int spanSize) {
                this.spanSize = spanSize;
            }

            protected ListBean(Parcel in) {
                cid = in.readString();
                hitCount = in.readString();
                id = in.readString();
                pubTime = in.readString();
                sourceUrl = in.readString();
                subTitle = in.readString();
                thumbnails = in.readString();
                title = in.readString();
                itemType = in.readInt();
                spanSize = in.readInt();
            }

            public static final Creator<ListBean> CREATOR = new Creator<ListBean>() {
                @Override
                public ListBean createFromParcel(Parcel in) {
                    return new ListBean(in);
                }

                @Override
                public ListBean[] newArray(int size) {
                    return new ListBean[size];
                }
            };

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(cid);
                dest.writeString(hitCount);
                dest.writeString(id);
                dest.writeString(pubTime);
                dest.writeString(sourceUrl);
                dest.writeString(subTitle);
                dest.writeString(thumbnails);
                dest.writeString(title);
            }

            @Override
            public int getItemType() {
                return 0;
            }

        }

    }

}
