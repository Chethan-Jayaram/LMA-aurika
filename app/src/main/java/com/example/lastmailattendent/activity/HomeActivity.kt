package com.example.lastmailattendent.activity


import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.lastmailattendent.R
import com.example.lastmailattendent.activityviewmodel.TicketsListViewModel
import com.example.lastmailattendent.adapter.TicketListAdapter
import com.example.lastmailattendent.helper.GlobalClass
import com.example.lastmailattendent.helper.OnclickListner
import com.example.lastmailattendent.helper.ScoketNotificationListner
import com.example.lastmailattendent.helper.TicketListListner
import com.example.lastmailattendent.responses.GeneralPojo
import com.example.lastmailattendent.responses.ticketlist.Data
import com.example.lastmailattendent.responses.ticketlist.TicketList
import com.example.lastmailattendent.util.hide
import com.example.lastmailattendent.util.show
import com.example.lastmailattendent.viewmodelfactoy.TicketViewModelFactory
import com.google.android.material.navigation.NavigationView
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator
import kotlinx.android.synthetic.main.activivty_home.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import kotlinx.android.synthetic.main.nav_header.*
import kotlinx.android.synthetic.main.nav_header.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import kotlin.properties.Delegates


class HomeActivity : AppCompatActivity(), TicketListListner, KodeinAware,
    NavigationView.OnNavigationItemSelectedListener,
    ScoketNotificationListner {

    override val kodein by kodein()
    private val factory: TicketViewModelFactory by instance()
    lateinit var listModelList: TicketsListViewModel
    lateinit var mReasonSialoug: AlertDialog
    var messageStr = ""
    lateinit var mAdapter: TicketListAdapter
    lateinit var myrecylcerview: RecyclerView
    lateinit var mediaPlayer: MediaPlayer
    val focusLock = Any()
    lateinit var audioManager: AudioManager
    lateinit var focusRequest: AudioFocusRequest
    var value by Delegates.notNull<Int>()
    var mFocusGranted: Boolean = false
    var res: Data? = null
    var viewGroup: ViewGroup? = null
    var dialogView: View? = null


    lateinit var headerLayout: View


    companion object {
        var notificationListner: ScoketNotificationListner? = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activivty_home)
        toolbar_title.setText("Active Tickets")
/*

        iv_profile.setImageDrawable(
            ContextCompat.getDrawable(
                applicationContext,
                R.drawable.ic_profile
            )
        )
*/


/*        nav_menu.setOnClickListener { v: View? ->
            hideKeybord()
         navdrawerOpenClose()
        }
        nav_view.setNavigationItemSelectedListener(this)*/

        nav_menu.visibility = View.GONE;
        mediaPlayer = MediaPlayer.create(this, R.raw.message)

        listModelList = ViewModelProvider(this, factory).get(TicketsListViewModel::class.java)

        listModelList.TicketListListner = this
        myrecylcerview = active_tickets_recycler_view



        swipe_to_refresh.setOnRefreshListener {
            try {
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.pause()
                }
                listModelList.getLatestTickets()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        iv_more.setOnClickListener {
            showPopUp(it)
        }
        headerLayout = nav_view.getHeaderView(0)
        headerLayout.setOnClickListener {
            Intent(this, EditProfileActivity::class.java).also {
                startActivity(it)
            }
        }
        /* mediaPlayer.setOnCompletionListener {
             checkForUnassignedTickets(listModelList.latestList)
         }*/
        headerLayout.user_name.setText(GlobalClass.mUserName)
        initialiseDialoug();
    }

    private fun navdrawerOpenClose() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else
            drawer_layout.openDrawer(GravityCompat.START)
    }

    private fun initialiseDialoug() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        viewGroup = this.findViewById(R.id.relative_lyt)
        //then we will inflate the custom alert dialog xml that we created
        dialogView = LayoutInflater.from(this)
            .inflate(R.layout.ticket_escaltion_alert_view, viewGroup, false)
        //Now we need an AlertDialog.Builder object
        val builder = AlertDialog.Builder(this)
        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView)
        //finally creating the alert dialog and displaying it
        mReasonSialoug = builder.create()

    }


    private fun showPopUp(view: View?) {
        try {
            var popup: PopupMenu? = null;
            popup = PopupMenu(this, view!!)
            popup.inflate(R.menu.menu_item)
            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->
                when (item!!.itemId) {
                    R.id.logout -> {
                        Intent(this, LoginActivity::class.java).also {
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(it)
                            finish()
                        }
                    }
                }
                true
            })

            popup.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun hideKeybord() {
        val imm =
            this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(nav_menu.windowToken, 0)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        try {
            drawer_layout.closeDrawer(GravityCompat.START)
            notificationListner = this
            Glide.with(this)
                .load(GlobalClass.url)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.profile_image)
                        .error(R.drawable.profile_image)
                )
                .into(headerLayout.img_profile_photo);
            listModelList.getLatestTickets()
            listModelList.connectWebSocket()
            audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN).run {
                    setAudioAttributes(AudioAttributes.Builder().run {
                        setUsage(AudioAttributes.USAGE_GAME)
                        setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        build()
                    })
                    build()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onPause() {
        super.onPause()
        try {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                audioManager.abandonAudioFocusRequest(focusRequest)
            } else {
                audioManager.abandonAudioFocus(null);
            }
            mFocusGranted = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onStarted() {
        progress_bar.show()
        active_tickets_recycler_view.visibility = View.INVISIBLE
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onSuccess(ticketList: TicketList) {
        progress_bar.hide()
        swipe_to_refresh.isRefreshing = false
        listModelList.ticketList?.let {
            it.observe(this, Observer {
                initRecyclerView(it)
            })
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun initRecyclerView(result: List<Data>) {
        try {
            mAdapter = TicketListAdapter(result, listModelList, object : OnclickListner {
                override fun onItemClicked(pos: Int) {
                    try {
                        val intent = Intent(baseContext, TicketDetailsActivity::class.java)
                        intent.putExtra("id", (result.get(pos).id).toString())
                        startActivity(intent)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })

            myrecylcerview.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = mAdapter
                itemAnimator = SlideInRightAnimator()
                getItemAnimator()?.setAddDuration(150);
                getItemAnimator()?.setRemoveDuration(150);
                getItemAnimator()?.setMoveDuration(150);
                getItemAnimator()?.setChangeDuration(150);
            }
            /* if(result.size>0) {
                 checkForUnassignedTickets(result)
             }*/
            active_tickets_recycler_view.visibility = View.VISIBLE
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun checkForUnassignedTickets(result: List<Data>) {
        try {
            if (!mediaPlayer.isPlaying) {
                requestAudioFocus()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && mFocusGranted) {
                    mediaPlayer.start()
                } else {
                    mediaPlayer.start()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun requestAudioFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            value = audioManager.requestAudioFocus(focusRequest)
            synchronized(focusLock) {
                when (value) {
                    AudioManager.AUDIOFOCUS_REQUEST_FAILED -> {
                        mFocusGranted = false
                        false
                    }
                    AudioManager.AUDIOFOCUS_REQUEST_GRANTED -> {
                        mFocusGranted = true
                        true
                    }
                    else -> false
                }
            }
        } else {
            audioManager.requestAudioFocus(
                null, AudioManager.STREAM_VOICE_CALL,
                AudioManager.AUDIOFOCUS_GAIN
            );
        }

    }


    override fun onFailure(message: String) {
        if (progress_bar.isShown) {
            progress_bar.hide()
        }


    }

    override fun onEscaltedReason(int: Int) {
        if (!mReasonSialoug.isShowing) {
            reasoningDailoug(int)
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onSocketTriggered(result: GeneralPojo) {
        try {

            if (result.message.assignee != null) {
                    if (listModelList.latestList.isEmpty()) {
                        var handler = Handler(Looper.getMainLooper())
                        listModelList.latestList.clear()
                        listModelList.latestList.add(0, result.message)
                        var Runnable = Runnable {
                            initRecyclerView(listModelList.latestList);
                        }
                        handler.post(Runnable);
                    } else if ((!listModelList.latestList.any { x -> x.id == result.message.id })
                        && !result.message.current_status.event_name.equals(
                            "complete",
                            ignoreCase = true
                        )
                    ) {
                        listModelList.latestList.add(0, result.message)
                        notifyItemChanged(listModelList.latestList)
                    } else if ((listModelList.latestList.any { x -> x.id == result.message.id })
                        && !result.message.current_status.event_name.equals(
                            "complete",
                            ignoreCase = true
                        )
                    ) {
                        res =
                            (listModelList.latestList.filter { it.id == result.message.id }.get(0))
                        listModelList.latestList.remove(res!!)
                        listModelList.latestList.add(0, result.message)
                        notifyItemChanged(listModelList.latestList)
                    } else if(result.message.current_status.event_name.equals(
                            "complete",
                            ignoreCase = true
                        )) {
                        res =
                            (listModelList.latestList.filter { it.id == result.message.id }.get(0))
                        var pos: Int = (listModelList.latestList.indexOf(res!!))
                        listModelList.latestList.remove(res!!)
                        myrecylcerview.apply {
                            adapter?.notifyItemRemoved(pos)
                        }
                       /* if (!result.message.current_status.event_name.equals(
                                "complete",
                                ignoreCase = true
                            )
                        ) {
                            listModelList.latestList.add(0, result.message)
                            notifyItemChanged(listModelList.latestList)
                        } else {
                            myrecylcerview.apply {
                                adapter?.notifyItemRemoved(pos)
                            }
                        }*/
                    }
                    checkForUnassignedTickets(listModelList.latestList)
                    var mainHandler = Handler(Looper.getMainLooper())

                    var myRunnable = Runnable {
                        notificationListner?.OnNotificationListner(result.message)
                    }
                    mainHandler.post(myRunnable);
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun notifyItemChanged(latestList: ArrayList<Data>) {
        myrecylcerview.apply {
            adapter?.notifyItemRangeChanged(0, latestList.size)
        }
    }


    private fun reasoningDailoug(int: Int) {
        try {
            val et_reason: EditText
            val btn_cancel: TextView
            val btn_submit: TextView

            btn_cancel = dialogView!!.findViewById(R.id.btn_cancel)
            et_reason = dialogView!!.findViewById(R.id.et_reason)
            btn_submit = dialogView!!.findViewById(R.id.btn_submit)
            et_reason.setText("")
            mReasonSialoug.show()
            mReasonSialoug.setCancelable(false)
            btn_cancel.setOnClickListener {
                mReasonSialoug.dismiss()
            }
            btn_submit.setOnClickListener {
                try {
                    if (!et_reason.text.toString().isEmpty()) {
                        messageStr = et_reason.text.toString()
                        listModelList.getMessage(messageStr, int)
                        mReasonSialoug.dismiss()
                    } else {
                        GlobalClass.ShowAlertDailog(this, "Alert", "Field cant be left blank")
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }

            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

    }

    override fun OnNotificationListner(res: Data) {
        GlobalClass.notifcation(getWindow().getDecorView().getRootView() as ViewGroup, res!!, this)
    }

    override fun onDestroy() {
        super.onDestroy()
        listModelList.mWebSocketClient.close()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.history -> {
                //   Toast.makeText(this, "History clicked", Toast.LENGTH_SHORT).show()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


}
