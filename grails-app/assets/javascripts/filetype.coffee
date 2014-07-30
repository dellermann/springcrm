#
# filetype.coffee
#
# Copyright (c) 2011-2014, Daniel Ellermann
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.
#
#= require jquery


$ = jQuery


# Immutable mapping for extensions to file types and file types to FontAwesome
# icons.
#
# @mixin
# @author   Daniel Ellermann
# @version  1.4
# @since    1.4
#
ExtDb = do ->

  # Mappings from file types to a space separated list of file extensions.
  # This object is initialized in the next anonymous function.
  #
  rawExtensions =
    archive: '0 7z 7z001 7z002 a00 a01 a02 ace alz ar arc arj ark b1 b64 ba bh bndl boo bz bz2 bza bzip bzip2 c00 c01 c02 c10 cb7 cba cbr cbt cbz cmp cp9 cpgz czip dar deb dgc dist dl_ drz dwz dz ecs efw egg gca gpk gz gz2 gza gzi gzip ha hbc hbc2 hbe hki hki1 hki2 hki3 ice ipg ish ita ize jam jarpack jgz jic kgb kz lbr lemon lha lqr lz lzh lzm lzma lzo lzx mcp mint mls mou mpkg mrt mv mzp oar oz packgz pae pak paq6 paq7 paq8 paq8f par par2 pax pbi pcv pea pet pit piz pkg prp psz pup pwa qda r0 r00 r01 r02 r03 r1 r2 r21 r30 rar rar5 rev rk rp9 rpm rz s00 s7z sar sea sen sfs sfx shar shk shr sit sitx sqx srep sy_ tar targz targz2 tarlzma tarxz taz tbz tbz2 tg tgz tlz tlzma txz tx_ tz uc2 ufsuzip uha uzip vem vsi w02 war xef xez xmcdz xx xz yz yz1 z z01 z02 z03 z04 zfsendtotarget zi zip zipx zix zl zoo zz'
    audio: '2sf 2sflib 3ga 4mp 669 6cm 8cm 8med 8svx a2b a2i a2m a2p a2t aa aa3 aac aax abc ac3 acd acd-bak acd-zip acm act adg adts afc agm agr ahx aif aifc aiff aimppl akp al alac alaw alc all als amf amr amxd amz aob aria ariax at3 atrac au aud aup avastsounds avr awb ay b4s band bap bdd bidule bonk brstm bun bwf bwg bww caf caff cda cdda cdg cdlx cdo cfa cfxr cgrp cidb ckb ckf cmf conform copy cpa cpr cts cwb cwp dcf dewf df2 dfc dig djr dls dmf dmsa dmse drg ds2 dsm dss dtm dts dtshd dvf dw dwa dwd ec3 efa efe efk efq efs efv emp emx emy enc esps evr expressionmap exs f2r f32 f3r f4a f64 fda fdp fev flac fls fsb fsc fsm fst fzf fzv g721 g723 g726 gbproj gbs gig gio gp5 gpbank groove gsm h0 hma hmi hsb iaa iff igp imf isma it iti itls its jo jo-7z k26 kar kfn kin kmp koz krz ksc ksf ksm kt2 kt3 ktp la lms lof logic lqt lso lvp lwv m1a m3u m3u8 m4a m4b m4p m4r ma1 mbr med mid midi mini2sf minincsf minipsf minipsf2 miniusf mka mlp mo3 mod mogg moi mp1 mp2 mp3 mpa mpc mpdp mpga mpu mp_ mscx mscz mss msv mt2 mt9 mte mti mtm mtp mu3 mus musa mux muz mwand mx3 mx4 mx5 mx5template mxl mxmf myr nbs nkb nkc nki nkm nks nkx nml nra nrt nsa nst ntn nvf nwc obw odm ofr oga ogg okt oma omf omx opus orc ove ovw pandora pca pcast pcg pcm peak pek pk pkf pls plst pna pno ppcx psf1 psf2 psm ptcop ptf ptm pts ptx pvc q1 q2 qcp r1m ra rad ram rax rbs rcy record rex rgrp rmi rmj rmm rmx rns rol rsn rso rti rvx rx2 s3i s3m s3z sap sbg sbi sc2 scs11 sd sd2 sd2f sdat sdii sds sdx seg seq ses sesx sf2 sfap0 sfk sfl sfpack shn sib slp smf smp snd sng snsf sonic sou sppack sprg spx sseq ssnd stap sts sw swa swav sxt syh syn syw syx tak tfmx thx tm8 tmc trak tta tun txw u8 uax ub ulaw ult ulw uni usf usflib uw uwf vag vap vc3 vdj vgm vlc vmd vmo voc vox voxal vpl vpm vqf vrf vsq vyf w01 w64 wand wav wave wax wem wfb wfd wfp wma wow wpk wproj wrk wtpl wtpt wus wut wv wvc wwu wyz xa xi xm xmz xrns xsb xsp xspf xwb ym yookoo zpa zpl zvd zvr'
    code: '$01 4db a a2w actx ada addin ads agi alb am am4 am5 am6 am7 ane anjuta apa applet appx appxsym appxupload aps ap_ arsc artproj as as2proj as3proj ascs asdb asi asm asvf au3 aut autoplay awk bas basex bb bbc bbproject bbprojectd bcp bdsproj bet bpg bpr brx bs2 bsc bsh buildpath bur c caproj capx cbl cbp cc ccgame ccn ccscc cd cfc cham chef ckbx class clips clw cob cod config cp cpb cpp cs csi csn csproj csx ctl ctp ctxt cu cxp cxx d dabriefcase daconfig dbml dbo dbpro dbproj dcproj dcu dcuil ddp dec def dep depend developerprofile deviceids dex df1 dfk dfm dgml dgsl dm1 dmd dob docset dox dpk dpkw dpl dpr dproj dres dsgm dsp dtd dylib ecp edm edml edmx el elc ent entitlements erb erl exp exw f f90 fbp fbp7 fbz7 feature fgl filters fimpp fla for fpm fpp framework frj frx fs fsi fsproj fsscript fsx ftl ftn fxc fxcproj fxl fxpl gameproj gar gbap gbas gdfmakerproject gemspec gitignore glade gld global gm gm6 gm81 gmd gmk gmo gnumakefile gorm groovy groupproj gs gs3 gsproj gszip gvy h hal has hcf hh hhh hpf hpp hrl hs hxx hydra i idb ide idl idt ilk iml inc inl ipch ipp isc ise iwz j jav java jed jl jnilib jpd jpr jsfl jsh jspf jsxinc kb kdevdlg kdevelop kdevprj kdmp l lbi lbs lds lgo lhs licenses licx lisp list lnt lol lproj lrdb lsp lsproj lua lxsproj m m4 magik mak make makefile mako markdn markdown md mdown mdzip mf mfa mfcribbon-ms mk ml mm mo mom mpr msha mshc mshi mvx mxml myapp nb0 nbc ncb ned neko nfm nib nim nk nqc nsh nsi nupkg nuspec nxc o oca octest ocx odl omo orderedtest os ow owl oxygene p p6 pas pb pbg pbj pbproj pbxbtree pbxproj pbxuser pch pcp pde pdm pfg pgx ph php php3 php4 php5 pickle pika pjx pl plc ple pli pltsuite pm pmq po pod pom ppc ppl ppu pri project proto psd1 psess psm1 ptl pty pwn pxd py pyd pym pyw pyx qpr qx r rb rbp rbvcp rbxs rc rc2 rdlc reb refresh res resjson resources resw resx rise rnc rodl rotest rotestresult rpy rsrc ru rul rws rwsnippet s s19 sas sb sbproj sbr scc scratch scriptsuite scriptterminology sdef setup sh sjava sll sln slogo slogt sltng sma smali snippet spec sqlproj src srcrpm ssc ssi storyboard sud suo svn-base swc swd sym t targets tcl tds testrunconfig testsettings textfactory tiprogram tk tld tlh tli tmlanguage tmproj tmproject tns tpu trx tt tu tur ui uml umlclass v vala var vbg vbp vbproj vbx vbz vc vcp vcproj vcxproj vdm vdp vdproj vgc vm vpc vsmacros vsmdi vsmproj vspf vsps vspscc vspx vssscc vsz vtm vtml vtv w w32 wdgtproj wdl wdw wiq worksheet wowproj wsc wsp xaml xcappdata xcarchive xcdatamodel xcdatamodeld xcode xcodeproj xcsnapshots xcworkspace xib xoml xpp xq xql xqm xquery xqy xsd xsx xt y yab yaml yml yml2 ymp ypr'
    document: 'abw awp awt aww bean bzabw dca dgs doc doc# docm docx docxml docz dot dotm dotx fdr fdt fdx fdxt fodt fwdn gdoc hwp kwd mwp ndoc odif odo odt ott pages pages-tef pdg psw pwd pwi rtf rtfd rtx sam sdw sla slagz stw sxg sxw tm tmd tmv uof uot wp wp4 wp5 wp6 wp7 wpa wpd wps wpt wpw wri wsd zabw'
    image: '2bp 360 411 73i 8pbs 8xi 9png abm accountpicture-ms acorn acr adc afx agif agp aic ais albm apd apm apng artwork asw avatar awd bld blkrt bm2 bmc bmf bmp bmx bmz brk bss bti btn c4 cals can cch cdc ce cff cimg cin cit colz cpbitmap cpc cpg cps cpt csf ct cut dc3 dcm dd dds dgt dib dicom djv djvu dm3 dmi dpx dt2 dtw dvl ecw epf epi exr fac face fal fax fbm fgd fit fits fll fpg fpos fpx fts fxs g3 gbr gcdp gfb gfie ggr gif gih gim gmbck gmspr gro grob gry hdp hdr hdrp hf hpi hr hrf i3d ic1 ic2 ic3 icb icn icon icpr ilbm imj imt ind info int iphotoproject ipick ipx iss itc2 ithmb ivue iwi j2c j2k jas jb2 jbf jbig jbig2 jbmp jbr jfi jfif jia jif jiff jng jp2 jpc jpe jpeg jpf jpg jpg2 jps jpx jtf jwl jxr kdc kdi kdk kfx kic kodak l01 lbm lif ljp mac mbm mcs mic mip mix mng mnr mpf mpo mrb mrxs msk myl nap ncr nct ncw neo nlm nmp oc3 oc4 oc5 oci odi oplc ota otb oti pap pat pbm pc1 pc2 pcd pcx pdd pdn pe4 pfi pfr pgf pgm pi pi1 pi2 pi3 pi4 pi5 pi6 pic picnc pict pictclipping pix pixadex pjpeg pjpg pm3 pmg png pni pnm pnt pntg pov pp4 pp5 ppm prw psb psd psdx pse psp pspbrush pspimage ptg pvr pwc pwp pxicon pxm pxr pza pzp pzs qmg qti qtif ras rcu rgb ric rif riff rix rle rli rpf rri rs rsb rsr s2mv sai scp sct scu sdg sdr sep sff sfw sgi shg sid skitch skm skypeemoticonset sob spe spiff spj spp spu sr sumo sun suniff sup sva t2b taac tb0 tbn tfc tg4 tga thm thumb tif tiff tm2 tn tn1 tn2 tn3 tny trif tub ufo upf urt usertile-ms vda vff vic viff vpe vrphoto vss wb1 wbc wbd wbm wbmp wbz wdp webp wi wic wmp wpe wvl xbm xcf xpm xwd ysp yuv zif'
    pdf: 'dvi pdf'
    presentation: 'apxl dpt fodp gslides key-tef kpp kpr odp otp pez potm potx ppt pptm pptx ppz prd prs prz sdd show shw sti sxi uop wnk'
    spreadsheet: '123 aws dfg dis edx edxz fcs fods gsheet gtable ks nam nb nmbtemplate numbers numbers-tef ods ots pmv qpw sdc stc sxc uos wki wks wku wq1 wq2 xl xlr xls xlsb xlshtml xlsm xlsmhtml xlsx xlthtml xltm xltx'
    text: '1st ac aim ans apt asc ascii aty bad bbs bdp bdr bib bli bna boc btd charset chart chord clg cnm crwl cws cyi diz dne dpf dsv dvi dx eml emlx emulecollection eng err esd etf etx euc faq fbl fcf fdf flr frg frt gpd gpn gsd gthr gv hbk hht hvc hz iil ipspot jarvis jis joe jp1 jrtf kes klg knt kon latex lbt linx lis log lp2 lst ltr ltx lue luf lwp lxfml lyx man mbox mcw md md5txt me mell mellel mmo mnt msg msw mtx mwp nfo njx note notes now nwctxt ocr ofl opd openbsd ort p7s pfs pjt plg pml pvj pwdp pwdpl qdl qvd qvt readme rep ris rst rzk rzn s01 s02 safetext sami save scriv scrivx scw sdm sgm sig skcard sms ssa story strings sty sub sublime-project sublime-workspace template tex text textclipping thp tpc tsc tvj txa txt u3i unauth unx upd utf8 utxt vct vnt vw webdoc wtx xbdoc xbplate xdl xml xsd xsl xslt xwp xy xy3 xyp xyw zw'
    video: '264 3g2 3gp 3gp2 3gpp 3gpp2 3mm 3p2 60d 787 890 aaf aec aep aepx aet aetx ajp ale amc amv amx apz aqt arcut arf asf asx avb avc avchd avd avi avp avs avv awlive axm bdm bdmv bdt2 bdt3 bik bmk bnp bs4 bsf bvr byu camproj camrec camv ced cine clpi cmmp cmmtpl cmproj cmrec cpi cvc cx3 d2v d3v dav dce dck ddat dir div divx dlx dmsd dmsd3d dmsm dmsm3d dmss dmx dnc dpa dpg dream dv dv-avi dv4 dvdmedia dvr dvr-ms dvx dxr dzm dzp dzt edl evo eye f4f f4p f4v fbr fbz fcp fcproject flc flh fli flv fmv ftc gcs gfp gl gmt gom grasp gts gvi gvp h264 hdmov hdv hkm ifo imovieproj imovieproject ircp ismc ismclip ismv iva ivf ivr ivs izz izzy jmv jss jts jtv k3g kdenlive kmv ktn lrec lrv lsx m15 m1pg m1v m21 m2a m2p m2s m2t m2ts m2v m4e m4u m4v m75 mani meta mgv mj2 mjp mjpg mk3d mkv mmm mmv mnv mob modd moff moov mov movie mp21 mp2v mp4 mp4infovid mp4v mpe mpeg mpeg1 mpeg4 mpg mpg2 mpgindex mpl mpls mpsub mpv mpv2 mqv msdvd mswmm mts mtv mvb mvd mve mvex mvp mvy mxf mxv mys ncor nsv nut nuv nvc ogm ogv ogx orv osp otrkey pds pgi photoshow piv pjs playlist plproj ppj prel pro4dvd pro5dvd proqc prproj prtl psh pssd pva pxv qt qtch qtindex qtl qtm qtz r3d rcd rec rm rmd rmp rms rmv rmvb roq rsx rum rv rvid rvl sbt scm screenflow sdv sedprj sfvidcap siv smi smil smk sml smv snagproj spa sqz srt ssm stx svi swf swi swt tda3mt tivo tix tod tp tp0 tpd tpr trp ts tsp ttxt tvs usm vc1 vcpf vcr vcv vdo vdr veg vep vf vft vfw vfz vgz vid video viewlet vivo vlab vob vp3 vp6 vp7 vro vs4 vse vsp vvf wcp webm wlmp wm wmd wmmp wmv wmx wot wp3 wpl wtv wvx xej xel xesc xfl xlmv xmv xvid y4m yog zeg'

  # Mapping from file extensions to file types.  This object is initialized
  # in the next anonymous function.
  #
  extensions = {}

  # Mapping from file types to FontAwesome CSS icon class names.
  #
  types =
    archive: 'fa-file-archive-o'
    audio: 'fa-file-audio-o'
    code: 'fa-file-code-o'
    document: 'fa-file-word-o'
    file: 'fa-file-o'
    image: 'fa-file-image-o'
    pdf: 'fa-file-pdf-o'
    presentation: 'fa-file-powerpoint-o'
    spreadsheet: 'fa-file-excel-o'
    text: 'fa-file-text-o'
    video: 'fa-file-video-o'

  do ->
    for type, extensionString of rawExtensions
      extensionList = extensionString.split ' '
      for ext in extensionList
        extensions[ext] = type

  # Gets the FontAwesome CSS icon class name for the given file extension.
  #
  # @param {String} ext the given file extension
  # @return {String}    the CSS class name
  #
  getIcon = (ext) ->
    getTypeIcon getType ext

  # Gets the file type associated to the given file extension.
  #
  # @param {String} ext the given file extension
  # @return {String}    the file type or `file` if the file extension is unknown
  #
  getType = (ext) ->
    extensions[ext.toLowerCase()] ? 'file'

  # Gets the FontAwesome CSS icon class name for the given file type.
  #
  # @param {String} ext the given file type
  # @return {String}    the CSS class name
  #
  getTypeIcon = (type) ->
    types[type]

  getIcon: getIcon
  getType: getType
  getTypeIcon: getTypeIcon


# This mixin contains static jQuery extensions to obtain file types.
#
# @mixin
# @author   Daniel Ellermann
# @version  1.4
# @since    1.4
#
JQueryStaticExt =

  # Gets the FontAwesome icon for the given file extension.
  #
  # @param {String} ext the given file extension
  # @return {String}    the FontAwesome CSS icon class name
  #
  fileicon: (ext) ->
    ExtDb.getIcon ext

  # Gets the type of the file for the given file extension.
  #
  # @param {String} ext the given file extension
  # @return {String}    the file type or `file` if the file extension is unknown
  #
  filetype: (ext) ->
    ExtDb.getType ext

$.extend JQueryStaticExt
