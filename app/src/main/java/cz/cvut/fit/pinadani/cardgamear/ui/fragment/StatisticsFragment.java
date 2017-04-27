package cz.cvut.fit.pinadani.cardgamear.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.cvut.fit.pinadani.cardgamear.R;
import cz.cvut.fit.pinadani.cardgamear.model.User;
import cz.cvut.fit.pinadani.cardgamear.mvp.presenter.StatisticsPresenter;
import cz.cvut.fit.pinadani.cardgamear.mvp.view.IStatisticsView;
import cz.cvut.fit.pinadani.cardgamear.ui.adapter.StatisticsAdapter;
import cz.cvut.fit.pinadani.cardgamear.ui.fragment.base.BaseNucleusFragment;
import nucleus.factory.RequiresPresenter;

/**
 * Fragment to statistics
 * Created by Daniel Pina
 **/
@RequiresPresenter(StatisticsPresenter.class)
public class StatisticsFragment extends BaseNucleusFragment<StatisticsPresenter>
        implements IStatisticsView {
    public static final String TAG = StatisticsFragment.class.getName();

    StatisticsAdapter mAdapter;

    @Bind(R.id.list)
    ListView mListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        ButterKnife.bind(this, view);

        mAdapter = new StatisticsAdapter(getActivity(), R.layout.listview_item_row, new
                ArrayList<>());
        mListView.setAdapter(mAdapter);
        return view;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.score);
    }

    @Override
    public void pressBack() {
    }


    @Override
    protected void initAB() {
        baseSettingsAB();
    }

    @Override
    public void setData(ArrayList<User> users) {
        mAdapter.clear();
        mAdapter.addAll(users);
        mAdapter.notifyDataSetChanged();
    }
}
