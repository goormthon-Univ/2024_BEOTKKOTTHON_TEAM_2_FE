package com.mukatlist.mukatlist.initSetting

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Checkbox
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mukatlist.mukatlist.HomeActivity
import com.mukatlist.mukatlist.LoginActivity
import com.mukatlist.mukatlist.MainActivity
import com.mukatlist.mukatlist.R
import com.mukatlist.mukatlist.SignInActivity
import com.mukatlist.mukatlist.data.DumiUniData
import com.mukatlist.mukatlist.data.TopNavItem
import com.mukatlist.mukatlist.data.UniData
import com.mukatlist.mukatlist.ui.theme.Color_Selected
import com.mukatlist.mukatlist.ui.theme.Color_Unelected
import com.mukatlist.mukatlist.ui.theme.MUKATLIST
import com.mukatlist.mukatlist.ui.theme.MukatlistTheme
import com.mukatlist.mukatlist.ui.theme.Orange01
import com.mukatlist.mukatlist.ui.theme.Search_Bar
import com.mukatlist.mukatlist.MainApplication
import com.mukatlist.mukatlist.ui.theme.font_pt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

var check = 0
var check_Uni = ""

@Composable
fun search_university(
    navController: NavHostController
){
    val allunidata = DumiUniData().allData()
    val context = LocalContext.current
    val activity = (LocalContext.current as? Activity)

    Column (
        modifier = Modifier.fillMaxSize()
    ){
        TopSearchBar()

        // 검색어
        var textState by remember { mutableStateOf("") }


        // 대학교 검색
        BasicTextField(
            value = textState,
            onValueChange = {if (it.length > 30) textState else textState = it},
            textStyle = TextStyle(fontSize = 20.sp),
            decorationBox = {innerTextField ->
                Row (modifier = Modifier
                    .background(
                        color = Search_Bar,
                        shape = RoundedCornerShape(size = 20.dp)
                    )
                    .fillMaxWidth()
                    .padding(all = 10.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = null,
                        tint = Color_Unelected
                    )
                    Spacer(modifier = Modifier.padding(all = 10.dp))
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ){
                        if(textState.isEmpty())
                            Text(
                                text = "대학교를 입력해주세요.",
                                color = Color_Unelected,
                                modifier = Modifier.fillMaxWidth()
                            )
                        innerTextField()
                    }
                }
            },
            modifier = Modifier.padding(bottom = 3.dp)
        )

        //-----------------------------------------------

        Box (
            modifier = Modifier
                .background(Color.White)
                .padding(all = 10.dp)
        ){
            LazyColumn (
                contentPadding = PaddingValues(start = 10.dp, end = 10.dp, bottom = 10.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.fillMaxSize()
                    .align(Alignment.Center)
            ){
                items(allunidata){
                    UniList(it)
                }
            }
            Button(
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(Orange01),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(Color.White),
                onClick={
                    if(check_Uni == ""){
                        Toast.makeText(context,"대학을 선택해주세요!", Toast.LENGTH_LONG).show()
                    }
                    else{
                        CoroutineScope(Dispatchers.IO).launch {
                            MainApplication.getInstance().getDataStore().setText_uni(check_Uni)
                        }
                        activity?.finish()
                        context.startActivity(Intent(context, HomeActivity::class.java))
                    }
                }
            ) {
                Text(text = "확인")
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopSearchBar() {
    Box(modifier = Modifier.fillMaxWidth())
    {
        CenterAlignedTopAppBar(
            title = {
                Image(
                    painter = painterResource(R.drawable.ic_mukat),
                    contentDescription = null
                )
            },
            navigationIcon = {
                IconButton(onClick = {/* DO SOMETHING*/ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = null,
                        modifier = Modifier
                            .width(20.dp)
                            .height(20.dp)
                    )
                }
            }
        )
    }
}


@Composable
fun UniList(unidata: UniData) {
    var checkedStatus by remember {
        mutableStateOf(false)
    }

    Row(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = checkedStatus,
            onCheckedChange = {
                Log.d("CheckBoxActivity", "checkBoxContainer: $it")
                if(checkedStatus == true && check == 1){
                    check = 0
                    check_Uni = ""
                    checkedStatus = it
                }
                else if (checkedStatus == false && check == 0){
                    check = 1
                    check_Uni = "{${unidata.nameKR}}"
                    checkedStatus = it
                }
            },

            modifier = Modifier.padding(end = 15.dp)
        )
        Column(

        ) {

            Text(
                text = unidata.nameKR,
                fontFamily = font_pt,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = unidata.nameEN,
                fontFamily = font_pt,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
internal fun search_university_preview(){
    val navController = rememberNavController()
    MukatlistTheme{
        search_university(navController)
    }
}

@Preview(showBackground = true)
@Composable
internal fun unilist_preview(){
    MukatlistTheme{
        UniList(UniData("순천향대학교", "SunChunHyang Uni"))
    }
}