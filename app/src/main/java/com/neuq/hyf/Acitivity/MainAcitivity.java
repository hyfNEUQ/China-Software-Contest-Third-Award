package com.neuq.hyf.Acitivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.neuq.hyf.Model.UserInfo;
import com.neuq.hyf.R;
import com.neuq.hyf.View.ConfigView;
import com.neuq.hyf.View.ForumView;
import com.neuq.hyf.View.ProductView;
import com.neuq.hyf.View.servierGuideView;
import com.neuq.hyf.View.SearcherView;
import com.sloop.fonts.FontsManager;

import java.util.ArrayList;

import devlight.io.library.ntb.NavigationTabBar;

public class MainAcitivity extends AppCompatActivity implements View.OnClickListener {
    private servierGuideView servierGuideView = null;
    private ProductView productView = null;
    private Context context = null;
    private View forumView, searcherView, serviceGiudeView;
    private UserInfo userInfo = UserInfo.getInstance();
    private MaterialSearchView searchView = null;
    private Toolbar toolbar;
    private SearcherView mySearcherView;
    private ForumView forumview = null;
    private ConfigView configView;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_samples_ntb);
        this.context = this;
        //把此Acitivity的字体设置成隶书
        FontsManager.initFormAssets(context, "fonts/st.TTF");
        FontsManager.changeFonts(this.getWindow().getDecorView());
        servierGuideView = new servierGuideView(this);
        //初始化view
        configView = new ConfigView(context);
        forumview = new ForumView(this.context);
        forumView = forumview.getView();
        searcherView = initSearcherView();
        serviceGiudeView = servierGuideView.getView();
        initUI();
    }

    protected void onStart() {
        super.onStart();

    }

    //用于初始化ViewPager
    private void initUI() {
        final ViewPager viewPager = findViewById(R.id.vp_horizontal_ntb);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public boolean isViewFromObject(final View view, final Object object) {
                return view.equals(object);
            }

            @Override
            public void destroyItem(final View container, final int position, final Object object) {
                ((ViewPager) container).removeView((View) object);
            }

            @Override
            public Object instantiateItem(final ViewGroup container, final int position) {
                View view = null;
                switch (position) {
                    case 0:
                        view = forumView;
                        break;
                    case 1:
                        view = searcherView;
                        break;
                    case 2:
                        view = serviceGiudeView;
                        break;
                    case 3:
                        view = configView.getView();

                        break;
                }
                container.addView(view);
                return view;
            }
        });
        final String[] colors = getResources().getStringArray(R.array.default_preview);

        final NavigationTabBar navigationTabBar = findViewById(R.id.ntb_horizontal);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.forum),
                        Color.parseColor(colors[0]))
                        .selectedIcon(getResources().getDrawable(R.drawable.forum))
                        .title("论坛")
                        .badgeTitle("forum")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.searcher_icon),
                        Color.parseColor(colors[1]))
//                        .selectedIcon(getResources().getDrawable(R.drawable.ic_eighth))
                        .title("浏览器")
                        .badgeTitle("searcher")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.qa),
                        Color.parseColor(colors[2]))
                        .selectedIcon(getResources().getDrawable(R.drawable.qa))
                        .title("问答机器人")
                        .badgeTitle("servier")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.config_icon),
                        Color.parseColor(colors[3]))
//                        .selectedIcon(getResources().getDrawable(R.drawable.ic_eighth))
                        .title("个人中心")
                        .badgeTitle("setting")
                        .build()
        );

        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, 0);
        navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                navigationTabBar.getModels().get(position).hideBadge();
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });

        navigationTabBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < navigationTabBar.getModels().size(); i++) {
                    final NavigationTabBar.Model model = navigationTabBar.getModels().get(i);
                    navigationTabBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            model.showBadge();
                        }
                    }, i * 100);
                }
            }
        }, 500);
    }

    //用于设置浏览器界面
    private View initSearcherView() {
        View view = LayoutInflater.from(
                getBaseContext()).inflate(R.layout.searcher_view, null, false);
        searchView = view.findViewById(R.id.search_view);
        toolbar = view.findViewById(R.id.toolbar1);
        toolbar.setTitle("搜索数控机床相关问题");
        setSupportActionBar(toolbar);
        mySearcherView = new SearcherView(view, context);
        mySearcherView.setListener();
        return view;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setMachineButton:
                Intent intent = new Intent();
                intent.setClass(this, ProductAcivity.class);
                this.startActivity(intent);
                break;
        }
    }
}
