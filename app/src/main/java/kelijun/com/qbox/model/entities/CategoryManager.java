package kelijun.com.qbox.model.entities;

import android.content.Context;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

import kelijun.com.qbox.R;
import kelijun.com.qbox.module.news_category.CategoryEntity;


public class CategoryManager {
    Context mContext;

    public CategoryManager(Context context) {
        mContext = context;
    }

    public List<CategoryEntity> getAllCategory() {
        List<CategoryEntity> categoryEntityList = new ArrayList<>();

        Resources resources = mContext.getResources();
        String[] nameArray = resources.getStringArray(R.array.category_name);
        String[] typeArray = resources.getStringArray(R.array.category_type);

        for (int i = 0; i < (nameArray.length > typeArray.length ? typeArray.length : nameArray.length); i++) {
            CategoryEntity categoryEntity = new CategoryEntity(null, nameArray[i], typeArray[i],i);
            categoryEntityList.add(categoryEntity);
        }

        return categoryEntityList;
    }
}
