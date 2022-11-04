package com.example.ankoko

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ankoko.databinding.ActivityMainBinding
import com.example.ankoko.databinding.MainRecyclerRowBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    // 제목을 담을 ArrayList
    val subjectList = ArrayList<String>()

    // 날짜를 담을 ArrayList
    val dateList = ArrayList<String>()

    // 메모의 번호를 담을 ArrayList
    val idxList = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // toolbar 를 설정한다.
        setSupportActionBar(binding.mainToolbar)
        title = "메모앱"

        // RecyclerView 셋팅
        val mainRecyclerAdapter = MainRecyclerAdapter()
        binding.mainRecycler.adapter = mainRecyclerAdapter
        binding.mainRecycler.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()

        // ArrayList 를 비워준다
        subjectList.clear()
        dateList.clear()
        idxList.clear()

        // 데이터베이스 오픈
        val helper = DBHelper(this)

        // 쿼리문
        val sql = """
            select memo_subject, memo_date, memo_idx
            from MemoTable
            order by memo_idx desc
        """.trimIndent()

        val c1 = helper.writableDatabase.rawQuery(sql, null)

        while (c1.moveToNext()) {
            // 컬럼 index 를 가져온다.
            val idx1 = c1.getColumnIndex("memo_subject")
            val idx2 = c1.getColumnIndex("memo_date")
            val idx3 = c1.getColumnIndex("memo_idx")

            // 데이터를 가져온다.
            val memoSubject = c1.getString(idx1)
            val memoDate = c1.getString(idx2)
            val memoIdx = c1.getInt(idx3)

            // 데이터를 담는다.
            subjectList.add(memoSubject)
            dateList.add(memoDate)
            idxList.add(memoIdx)

            // RecyclerView 에게 갱신하라고 명령한다.
            binding.mainRecycler.adapter?.notifyDataSetChanged()
        }

        c1.close()
    }

    // option 메뉴를 설정한다
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            // 추가 버튼
            R.id.main_menu_add -> {
                val memoAddIntent = Intent(this, MemoAddActivity::class.java)
                startActivity(memoAddIntent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    // RecyclerView 의 어댑터
    inner class MainRecyclerAdapter : RecyclerView.Adapter<MainRecyclerAdapter.ViewHolderClass>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
            val mainRecyclerBinding = MainRecyclerRowBinding.inflate(layoutInflater)
            val holder = ViewHolderClass(mainRecyclerBinding)

            // 생성되는 항목 View 의 가로 세로 길이를 설정한다.
            mainRecyclerBinding.root.layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            // 항목 View 에 이벤트를 설정한다
            mainRecyclerBinding.root.setOnClickListener(holder)

            return holder
        }

        override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
            holder.rowMenuSubject.text = subjectList[position]
            holder.rowMenuDate.text = dateList[position]
        }

        override fun getItemCount(): Int {
            return subjectList.size
        }

        // HolderClass
        inner class ViewHolderClass(mainRecyclerBinding: MainRecyclerRowBinding) :
            RecyclerView.ViewHolder(mainRecyclerBinding.root), View.OnClickListener {
            // View 의 주소값을 얻는다
            val rowMenuSubject = mainRecyclerBinding.memoSubject
            val rowMenuDate = mainRecyclerBinding.memoDate

            override fun onClick(p0: View?) {
                // Log.d("memo_app", "항목 클릭: $adapterPosition")

                // 현재 항목 글의 index 추출
                val memoIdx = idxList[adapterPosition]
                // Log.d("memo_app", "memo_idx $memoIdx")

                // 글 읽는 Activity 를 실행한다.
                val memoReadAdapter = Intent(baseContext, MemoReadActivity::class.java)
                memoReadAdapter.putExtra("memo_idx", memoIdx)
                startActivity(memoReadAdapter)
            }
        }
    }
}