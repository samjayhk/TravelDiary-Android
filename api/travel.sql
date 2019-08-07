-- phpMyAdmin SQL Dump
-- version 4.0.4.2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Apr 11, 2019 at 12:54 PM
-- Server version: 5.6.13
-- PHP Version: 5.4.17

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `travel`
--
CREATE DATABASE IF NOT EXISTS `travel` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `travel`;

-- --------------------------------------------------------

--
-- Table structure for table `comment`
--

CREATE TABLE IF NOT EXISTS `comment` (
  `cid` int(255) NOT NULL AUTO_INCREMENT,
  `pid` int(255) NOT NULL,
  `uid` int(11) NOT NULL,
  `comment` mediumtext COLLATE utf8_unicode_ci NOT NULL,
  `ctime` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `uptime` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`cid`),
  KEY `comment_fk1` (`pid`),
  KEY `comment_fk2` (`uid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=116 ;

--
-- Dumping data for table `comment`
--

INSERT INTO `comment` (`cid`, `pid`, `uid`, `comment`, `ctime`, `uptime`) VALUES
(115, 47, 1, 'India is an amazing place!', '1554296866382', '1554296866382');

-- --------------------------------------------------------

--
-- Table structure for table `post`
--

CREATE TABLE IF NOT EXISTS `post` (
  `pid` int(255) NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL,
  `subject` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `content` mediumtext COLLATE utf8_unicode_ci NOT NULL,
  `ptime` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `uptime` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `rate` int(255) NOT NULL DEFAULT '1',
  `tid` int(6) NOT NULL,
  PRIMARY KEY (`pid`),
  KEY `post_fk` (`uid`),
  KEY `FK_TID` (`tid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=51 ;

--
-- Dumping data for table `post`
--

INSERT INTO `post` (`pid`, `uid`, `subject`, `content`, `ptime`, `uptime`, `rate`, `tid`) VALUES
(44, 1, 'Cambodian Rock Band at the Oregon Shakespeare Festival (updated)', '<img src="https://www.everywhereist.com/wp-content/uploads/2019/03/14428692720_bba36e6863_b.jpg" />\n<br />\nMy trip to Cambodia feels so long ago \\u2013 more than five years \\u2013 that I have to remind myself that I was there. I look at the photos, and see myself looking so damn young that it almost startles me. I went to Cambodia with Nicci and we rode bikes to Angkor Wat and she would shout what vehicles were coming upon us and I would caution her about the monkeys that sat on the side of the road and we ate gelatinous fruits in air so damp and hot it felt like we were inside a sauna.\n<br />\nWe went to Tuol Sleng prison and we visited the Killing Fields in one day, and it felt like there was nothing left of us after that. Even as spectators, as tourists, as white women with expensive cameras, even with all the veils of privilege and distance to insulate you, the history of the Khmer Rouge will leave you feeling like a hollow shell.\n<br />\nI remember how the tiles in Tuol Sleng still appeared to be stained with blood (I couldn\\u2019t \\u2013 and still can\\u2019t \\u2013 say definitively if they were). How a rusted bed frame sat in one corner of the room and someone explained that this was the same bed that prisoners had been chained to and tortured on. How of the more than 20,000 people to pass through the prison, only 7 survived.\n<br />\nLater at the fields, we saw grounds that were sunken in from the decomposition of the bodies in the mass graves, and were instructed to stay on the paths less we disturb the remains. We were told that every time it rained, bits of cloth and bone and teeth would come to the surface and the groundskeepers would gently gather them up and put them in cases.\n<br />\nA tree at one edge of the fields bore a sign indicating that this is where children had been murdered (dashed against the trunk) in order to save bullets.\n<br />\nMonks walked the grounds, and butterflies fluttered in the heavy, still air. It remains one of the most terrifying places I\\u2019ve ever been.\n<br />\nI didn\\u2019t write too much about that trip to Cambodia. I started to, but it was hard to wrap my head around the experience, and returning home was so jarring, even though I\\u2019d only been gone for two short weeks divided across there and Vietnam. More and more time passed, and I neglected to talk about Cambodia. I wrote a book instead, with nary a mention of that trip.\n<br />\nPerhaps it was deliberate \\u2013 it\\u2019s easier to block it all out, to go on with the privilege of being comfortable and oblivious. The Khmer Rouge committed mass genocide, slaughtered millions, and destroying the country\\u2019s collective cultural, scientific, and artistic history. The U.S. played a critical role in their rise to power and securing their legitimacy and place on the global stage (we gave them weapons and money, we voted to give them a seat at the U.N., we helped to create the power vacuum and instability that enabled them to rise to power in the first place). I know these things without looking them up but somehow, in the intervening years, I\\u2019d allowed myself to forget.\n<br />\nEarlier this month, Rand and I were once again invited down to Ashland for the opening weekend of the Oregon Shakespeare Festival. One of the shows we\\u2019d be seeing was Cambodian Rock Band \\u2013 a musical. It had been described as heartbreaking and funny and uplifting, and I\\u2019d noticed that the positive adjectives outweighed the negative, and paired with my own self-denial that was enough to convince me that this play would not ruin me.\n<br />\nIt did, of course. I should have seen it coming, but I wanted so desperately to rewrite history.\n<br />\nThe characters in the play feel subject to this same force. We know how the story ends, we know who dies (virtually an entire generation), and yet we are given the illusion that things might somehow turn out differently. The tension is almost too much to bear. I told Rand that if I was watching it all unfold on my television screen, I\\u2019d have changed the channel. But plays and real life have that much in common \\u2013 usually, you can\\u2019t escape it. The only way out is through.\n<br />\nThe play centers around Neary, a young American woman working for an NGO that is trying to bring Duch \\u2013 a high-ranking officer who oversaw Tuol Sleng prison \\u2013 to justice. Without warning, her father, Chum, shows up in Phnom Penh for a visit \\u2013 his first time back in the country since he managed to flee the Khmer Rouge 30 years earlier. The play bounces backwards and forwards in time, from before the genocide, to the heart of it, to (nearly) present day. There are musical interludes (with tracks by Dengue Fever) that help to cut the extreme tension. But as soon as they are over, you are thrust back into the heart of it. (The symbolism was not lost on me \\u2013 art offers us an escape.)\n<br />\nI don\\u2019t want to reveal too much about the plot \\u2013 there are parts that you can anticipate as a viewer, but that\\u2019s a hard thing to avoid when a play is so deeply rooted in real-life horrors. Like I said: you know how the story ends. You simply have to watch the path unwind to see it get there. It\\u2019s agonizing at times.\n<br />\nThe second act is peppered with the sort of violence and gore that you\\u2019d be accustomed to finding in a work by Martin McDonagh. But unlike McDonagh\\u2019s work, it isn\\u2019t violence for violence\\u2019s sake, but rather based on facts. It is the parts of the play that are ripped from history that are most disquieting. The tiles of Tuol Sleng are perfectly recreated. Even the main antagonist \\u2013 Duch \\u2013 is a real person, and not some contrived bogeyman. He is currently still alive, and would be the only member of the Khmer Rouge to face an international tribunal for his part in the Cambodian genocide. Abandoning your suspension of disbelief won\\u2019t protect you from this play. The details may not have happened, most of the characters may be fictional, but the large scale horrors are all real.\n<br />\nThe performers in Cambodian Rock Band are excellent \\u2013 transitioning from musical numbers to intense dramatic performances and comedic scenes with a sort of ease that leaves you reeling (but that is something I have grown accustomed to with the OSF).  The show holds similar space in my heart as my visit to the prison and the Killing Fields did: I\\u2019m glad I went. I would tell others to do so, with the caveats that it is an intense and terrifying experience, one I will likely not repeat.\n<br />\nThere is a part of me that wants to push it all into the recesses of my memory and focus on lighter things, like the other wonderful shows we saw that weekend. But I\\u2019ve learned that ignoring history doesn\\u2019t change it. And sometimes the best way to honor the dead is to remember.', '1554294857527', '1554610068852', 1, 37),
(45, 1, 'There is No Such Thing As Closure', '<img src="https://www.everywhereist.com/wp-content/uploads/2019/03/20170316_125310-e1553890637894.jpg" /> <br /> I am scheduled to leave for Germany in several days. I have already told my husband that I don\\u2019t want to go, in a whining tone that stretches syllables out so far that the words they once formed are barely recognizable. As a woman nearing the aging of forty, this is how I am coping with the  death of my father, who passed away *checks calendar* \\u2026 a not insignificant amount of time ago. <br /> I never imagined losing a parent would be an easy thing. But I reasoned that the death of my orderly, logical, unsentimental father would be different. I had loved him, and in his own way, he had loved me. And now he was gone. I had felt sadness \\u2013 both the intense grief of the moment and the lingering aftermath of it. I felt the pang of finding reminders of him (a global stamp meant for a letter I never sent, a scrap of paper with his handwriting on it), witnessed the persistence of time that rudely passed on without him. I figured I had processed all of these things. Grief was something you push through and then it\\u2019s over, I thought, like a workout or a particularly terrible movie. You\\u2019ve done it. It\\u2019ll never crop up again. <br /> I made the mistake of thinking that grief would be a linear thing. It never is. <br /> Since before I could remember, my father and I lived on different continents. He came to visit my brother and me when we were small, dutifully staying in America for a month every year before returning to Bavaria. Neither Dad nor I seemed to enjoy the time we spent together (my father was never much a fan of children, and the sentiment appeared to be mutual) but I suppose we realized that it was the right thing to do. He never smiled in photos, but wore a scowl so enduring that all his descendants \\u2013 even my infant nephew \\u2013 have it. <br /> My brother and Dad, making the face that we all have. <br /> His visits became less frequent as I grew older. When he came to visit me the summer before I turned 16, it had been five awkward, pubescent years since his last visit, and his response was a wide-eyed, \\u201cWhat the hell happened.\\u201d Not so much a question, but an answer unto itself. <br /> It wasn\\u2019t until my mid-20s that my visits with my father approached a sort of regular schedule, thanks to my husband\\u2019s work. Each trip followed a similar pattern, and I suspected that my father, who\\u2019d spent 40 years in the same job and 60 with the same haircut (stalwarts which outlasted all three of his marriages), had always appreciated that. We arrived during the same time of year, we\\u2019d rent a car, drive to the house, explain to my stepmother that no, no, I was not pregnant, share a few meals together, walk through a few tiny Bavarian villages, and head home. Inevitably, we\\u2019d receive a few speeding tickets, forwarded to us from our rental car agency, after we got back. <br /> I suspect that the declines in my father\\u2019s health were a relatively gradual thing. But seeing him only once a year, he seemed to age at an accelerated rate, much as I had for him. I resisted the urge to ask him what the hell happened. One year he was there, and then another year, he wasn\\u2019t. <br /> My Dad\\u2019s workshop. 3 months after he had passed away and it looked exactly the same. <br /> I did not go to my father\\u2019s funeral. <br /> \\u201cYou have to,\\u201d my brother told me. \\u201cOnly people who hate their fathers don\\u2019t go to their funerals.\\u201d It was a litmus test of sorts, though I could easily see the flaws in the logic: going to Dad\\u2019s funeral wouldn\\u2019t prove that I loved him, but missing it would prove that I did not. <br /> The dying rarely ever consult anyone on when would be a good time to depart the earth, and my dad was never big on conveniencing others. His timing was terrible. He died in the middle of a snowy December and his funeral was set for the week of Christmas. Getting there would be near impossible. <br /> My father had already been cremated, though no one was certain whether or not this was in accordance with his wishes. Dad didn\\u2019t make them known, because doing so would have required that he talk about his own impending death. My stepmother and I don\\u2019t speak a common language, so through my stepsister I asked if the funeral could be delayed for a few weeks so I could make it out. Through my stepsister, I was told that the funeral would not be delayed. Through my stepsister, I was also told that the request was not appreciated, either. Through my stepsister, I said nothing in reply. It was the worst conversation I\\u2019ve ever not had. <br /> When I finally realized I wouldn\\u2019t be able to make it \\u2013 the reality taking form slowly like a Polaroid, I wondered what the ramifications of it would be. I don\\u2019t know if it would have made a difference. Even if I\\u2019d gone, I\\u2019d still have to learn that grief was something that didn\\u2019t have precise lines of demarcation. <br /> Months later, I would finally see his grave for the first time \\u2013 a shockingly organic crag of rock (Bavarian aesthetics are different than American ones) that he shared with his most recent mother-in-law, whom he disliked intensely \\u2013 and assumed it would offer me some measure of that thing we call closure. But to echo a friend of mine who recently lost his wife: closure is a myth. Death not a finite thing that we get over. Some things stay with us, and after enough time, we simply get used to weight of them. <br /> The problem with losing someone who has spent a lifetime on another continent is that the weight of that grief is so inconsistent. You don\\u2019t just lose them once. You lose time every time you forget that they are gone, which is often. On Saturday nights, I still find myself double-checking to make sure the ringer on my phone is turned down, remembering my father\\u2019s proclivity to call me at 7:00am on Sunday mornings. (\\u201cWhy the hell are you sleeping?\\u201d he would ask, irritated. In reply, I would calmly explain the concept of time zones and weekends to him.) When I realize he will never wake me up at an ungodly hour on a weekend, I break down in tears. It feels like my own muscle memory has betrayed me. <br /> There lies all my hesitation for this trip \\u2013 I\\u2019ll be constantly confronted with the reality that my father is gone, and yet the loss doesn\\u2019t begin or end there. It carries on as it always does, popping up when I least expect it. One moment I\\u2019m fine, the next I\\u2019m not. I go to shut off my phone\\u2019s ringer in the middle of the night, and when my husband finds me much later, it is still in my hand. <br /> It\\u2019s been years, but it doesn\\u2019t matter. Some cuts feels fresh because you\\u2019ve just remembered they\\u2019re there.', '1554295222470', '1554295222470', 1, 80),
(46, 1, 'Emergency Porchetta, Canelli, Italy (updated)', '<img src="https://www.everywhereist.com/wp-content/uploads/2018/11/IMG_20181111_165228-e1543527714559.jpg" /> <br /> The emergency porchetta was my favorite part of the trip. <br /> That is a strange thing to say for a lot of reasons. For one, I don\\u2019t know if the concept of emergency porchetta is widely known. A Google search for the term reveals four results, the most salient of which is someone looking for cooking advice. \\u201cEmergency porchetta question\\u201d they write. But the urgency seems to come from the nature of the query itself, and not the hunk of roast pork it pertains to. <br /> For another, we had a great trip. Despite the endless rain and the grey skies and the hotel mishaps and the projectile vomiting on the side of the road in Genoa (my apologies to all who witnessed it), we had a wonderful week in the north of Italy. So many things happened that reminded me of why I love to travel. <br /> But damn it, that porchetta was everything. <br /> It could have ended differently \\u2013 all travel stories can. That\\u2019s what travel is supposed to do \\u2013 to open up a door to a thousand possibilities. And when I look back on the scene \\u2013 the four of us wandering through a market in Canelli, the air chilly and damp, the sky that deep blue-grey that it always is in the early evenings of late fall, when the sun has never bothered coming out \\u2013 it could have gone terribly awry. It was crowded and we were tired and Oli was hungry \\u2013 that urgent hunger that comes on quick and catches you off guard. <br /> Canelli from above. <br /> And damn it, Oli is a picky eater. I mean picky. We passed bar after bar, but they only served pasta (which he doesn\\u2019t eat because he\\u2019s an alien), or no food at all, or weren\\u2019t open yet. And then we came across a stall where a man was selling all manner of salumi. A vegeterian nightmare of every sort of cured meat you could imagine. And there, at the center, a roast of pork, a giant thing the size of a not-insubstantial child. <br /> \\u201cThey have porchetta.\\u201d I offered. It seemed like a long shot. <br /> \\u201cYup,\\u201d Oli said, with zero hesitation. \\u201cThat works.\\u201d Because Oli is picky, except when he isn\\u2019t at all. <br /> I told the man behind the table that we wanted some, and he indicated with his knife the thickness of a cut. <br /> No, I said, that\\u2019s too much. <br /> No, Oli corrected me, that\\u2019s just fine. <br /> Sometimes you don\\u2019t realize the scale of something until it is in your hands, and it was only when the man wrapped it up and we saw the price that we realized that Oli had just paid 20 Euros for half a kilo of porchetta. Not an unreasonable price, mind you. But you know, it was half a kilo of porchetta. <br /> The face of man who has just purchased a pound of roast pork. <br /> Oli stood in the middle of the crowd, and pulled off a chunk. <br /> \\u201cOh my god,\\u201d he said. \\u201cHave some.\\u201d <br /> And we all stood with him, picking at it with our fingers in the twilight, dubbing it \\u201cemergency porchetta.\\u201d We returned to the phrase again and again \\u2013 the accidental purchasing of a pound of roasted pork on a chilly night. The sort of thing that turns your evening from potentially disastrous to absurdly memorable. That sort of unpredictable, unrepeatable moment that will forever be grander than the sum of its parts.', '1554295428225', '1554610045584', 1, 106),
(47, 1, 'Transportation in India: What the Tuk-tuk?!', '<p class="ql-align-center"><a href="https://travelingcanucks.com/wp-content/uploads/2010/06/India-Tuk-Tuk.jpg" target="_blank" style="color: rgb(255, 255, 255); background-color: inherit;"><img src="https://travelingcanucks.com/wp-content/uploads/2010/06/India-Tuk-Tuk-1024x683.jpg" height="467" width="700"></a></p><h2 class="ql-align-center"><span style="color: rgb(255, 255, 255);">Transportation in India \\u2013 What the Tuk Tuk?!</span></h2><p class="ql-align-center"><span style="color: rgb(255, 255, 255);">We originally published this post back in 2010, after our first trip to India. We spent 5 weeks backpacking across Northern&nbsp;</span><a href="https://travelingcanucks.com/travel/asia/india/" target="_blank" style="color: rgb(255, 255, 255); background-color: inherit;">India</a><span style="color: rgb(255, 255, 255);">. I actually wrote most of this post on a moleskine notepad while traveling on an overnight train from Udaipur to Mumbai. Yes, an actual pen and paper!</span></p><p class="ql-align-center"><span style="color: rgb(255, 255, 255);">I was cleaning up some old posts and I stumbled on this one. I thought it would be fun to re-publish, as most of you likely have not read this one.</span></p><p class="ql-align-center"><br></p><p class="ql-align-center"><br></p><h4 class="ql-align-center"><span style="color: rgb(255, 255, 255);">If you\\u2019ve visited India this one should bring back some memories!</span></h4><p class="ql-align-center"><a href="https://travelingcanucks.com/wp-content/uploads/2013/07/jodhpur-rajasthan-india-27.jpg" target="_blank" style="color: rgb(255, 255, 255); background-color: inherit;"><img src="https://travelingcanucks.com/wp-content/uploads/2013/07/jodhpur-rajasthan-india-27.jpg" height="407" width="610"></a></p><h2 class="ql-align-center"><span style="color: rgb(255, 255, 255);">Transportation in India is unlike anywhere else</span></h2><p class="ql-align-center"><span style="color: rgb(255, 255, 255);">Travel and transportation through India can be challenging. At times, it can be a frightening ordeal that leaves you speechless, questioning why you would subject yourself to such unnecessary distress.</span></p><p class="ql-align-center"><br></p><p class="ql-align-center"><br></p><h4 class="ql-align-center"><span style="color: rgb(255, 255, 255);">Let me walk you through a typical tuk-tuk experience.</span></h4><p class="ql-align-center"><span style="color: rgb(255, 255, 255);">First, the majority of tuk tuks (aka auto-rickshaws)&nbsp;are falling apart and should not be allowed on the road. They\\u2019re basically a lawn mower with a rusty metal bubble on top, hardly the type of vehicle you\\u2019d want to get into an accident with.</span></p><p class="ql-align-center"><span style="color: rgb(255, 255, 255);">Second, we\\u2019re not convinced that&nbsp;the majority of&nbsp;drivers even have driver\\u2019s licenses; and if they do, the government should seriously consider beefing up the testing and qualifications process.</span></p><p class="ql-align-center"><span style="color: rgb(255, 255, 255);">Finally, drivers must learn to decipher which way the flow of traffic is going\\u2026 and stick to that direction. That means do not cross into oncoming traffic dummy!</span></p><p class="ql-align-center"><a href="https://travelingcanucks.com/wp-content/uploads/2012/11/india12.jpg" target="_blank" style="color: rgb(255, 255, 255); background-color: inherit;"><img src="https://travelingcanucks.com/wp-content/uploads/2012/11/india12.jpg" height="401" width="600"></a></p><h2 class="ql-align-center"><span style="color: rgb(255, 255, 255);">The Tuk Tuk experience in India</span></h2><p class="ql-align-center"><span style="color: rgb(255, 255, 255);">After firmly negotiating the fare you climb into the dirty, rusty auto-rickshaw careful not to cut yourself on loose metal. You then reach into your pocket, take out your hand towel and cover your mouth to protect yourself from the never ending clouds of foul black emissions.</span></p><p class="ql-align-center"><span style="color: rgb(255, 255, 255);">The tuk-tuk rolls forward,&nbsp;you hold on tight and keep your eyes focused straight ahead.</span></p><p class="ql-align-center"><span style="color: rgb(255, 255, 255);">The tuk-tuk forces its way down a narrow beat-up street, cautious of the large potholes and never ending stream of pedestrians. It randomly bounces from one lane to another with no regard for what\\u2019s going on behind.</span></p><p class="ql-align-center"><a href="https://travelingcanucks.com/wp-content/uploads/2010/06/Agra-India-Tuk-Tuk.jpg" target="_blank" style="color: rgb(255, 255, 255); background-color: inherit;"><img src="https://travelingcanucks.com/wp-content/uploads/2010/06/Agra-India-Tuk-Tuk-1024x682.jpg" height="433" width="650"></a></p><p class="ql-align-center"><span style="color: rgb(255, 255, 255);">A car in front is driving in the center lane\\u2026 oncoming motorcycles are forced to use the dusty shoulder instead. There isn\\u2019t really a need for road lines, nobody uses them anyways.</span></p><p class="ql-align-center"><span style="color: rgb(255, 255, 255);">The tuk tuk speeds up as it approaches the looming intersection.</span></p><p class="ql-align-center"><span style="color: rgb(255, 255, 255);">You hang on tighter, knowing there is nothing you can do at this point\\u2026 you\\u2019re committed to this journey.&nbsp;At the last minute, the calm driver slams on the brakes and makes a jerky right turn, squeezing between a bus and a pack of masked motorcyclists.</span></p><p class="ql-align-center"><span style="color: rgb(255, 255, 255);">The decrepit bus spits up a cloud of black smoke that fills the cab of the auto-rickshaw. You cough and sweep the cloud away with your hand but it\\u2019s no use, you\\u2019ve already swallowed the burning diesel fumes.</span></p><p class="ql-align-center"><a href="https://travelingcanucks.com/wp-content/uploads/2012/11/india03.jpg" target="_blank" style="color: rgb(255, 255, 255); background-color: inherit;"><img src="https://travelingcanucks.com/wp-content/uploads/2012/11/india03.jpg" height="401" width="600"></a></p><h3 class="ql-align-center"><span style="color: rgb(255, 255, 255);">Most intersections do not have traffic lights.</span></h3><p class="ql-align-center"><span style="color: rgb(255, 255, 255);">The flow of traffic rarely stops. Some are roundabouts, but mostly it\\u2019s the standard crossroads. Street signs are mostly a&nbsp;</span><em style="color: rgb(255, 255, 255);">guideline</em><span style="color: rgb(255, 255, 255);">.</span></p><p class="ql-align-center"><span style="color: rgb(255, 255, 255);">The driver inches his way into the chaotic intersection, continually beeping his horn and cutting off motorcyclists and overstuffed jeeps with passengers hanging out the rear window.</span></p><p class="ql-align-center"><span style="color: rgb(255, 255, 255);">There is no protocol.</span></p><p class="ql-align-center"><span style="color: rgb(255, 255, 255);">It\\u2019s first come first serve and the bigger vehicles win. Every time.&nbsp;</span></p><p class="ql-align-center"><span style="color: rgb(255, 255, 255);">Glancing over your shoulder,&nbsp;you&nbsp;see another tuk-tuk rapidly approaching at full speed.</span></p><p class="ql-align-center"><br></p><p class="ql-align-center"><br></p><h3 class="ql-align-center"><span style="color: rgb(255, 255, 255);">You close your eyes and brace for impact.</span></h3><p class="ql-align-center"><span style="color: rgb(255, 255, 255);">He hits the brakes and comes to within an inch of your vehicle; the oncoming driver gives you a big smile, thinking \\u201cyou must not be from around here\\u201d.</span></p><p class="ql-align-center"><span style="color: rgb(255, 255, 255);">He chuckles and moves on. His passengers stare at you with strange curiousity.</span></p><p class="ql-align-center"><a href="https://travelingcanucks.com/wp-content/uploads/2012/04/cow-india.jpg" target="_blank" style="color: rgb(255, 255, 255); background-color: inherit;"><img src="https://travelingcanucks.com/wp-content/uploads/2012/04/cow-india-1024x684.jpg" height="435" width="650"></a></p><h3 class="ql-align-center"><span style="color: rgb(255, 255, 255);">The&nbsp;driver honks at a dilapidated truck three times.&nbsp;</span></h3><p class="ql-align-center"><span style="color: rgb(255, 255, 255);">Why three times, you&nbsp;ask?&nbsp;It doesn\\u2019t matter.&nbsp;It\\u2019s like the Wild West and drivers have itchy trigger fingers.</span></p><p class="ql-align-center"><span style="color: rgb(255, 255, 255);">There is a commotion twenty meters ahead. Vehicles are swerving to avoid something.&nbsp;</span></p><p class="ql-align-center"><span style="color: rgb(255, 255, 255);">You stretch your neck up to see&nbsp;what\\u2019s going on.&nbsp;What\\u2019s blocking the road?</span></p><p class="ql-align-center"><span style="color: rgb(255, 255, 255);">Of course, it\\u2019s a pair of large brown cows sitting in the middle of the busy intersection!</span></p><p class="ql-align-center"><span style="color: rgb(255, 255, 255);">They gaze at the tuk-tuk&nbsp;as it passes&nbsp;by, oblivious to their dangerous choice of resting place. You do double take\\u2026 are they really chewing on a dirty plastic bag?&nbsp;Oops\\u2026 the tuk-tuk just ran over a cow patty.</span></p><p class="ql-align-center"><span style="color: rgb(255, 255, 255);">The driver smiles, then spits red slime out of his mouth. You watch it blow in the wind until it splats on the road. Gross.</span></p><p class="ql-align-center"><span style="color: rgb(255, 255, 255);"><img src="https://travelingcanucks.com/wp-content/uploads/2010/06/streets-of-agra-city-india-768x1024.jpg" height="733" width="550"></span></p><h3 class="ql-align-center"><span style="color: rgb(255, 255, 255);">Traffic picks up again.</span></h3><p class="ql-align-center"><span style="color: rgb(255, 255, 255);">So does the speedometer. Just kidding, there are no speedometers!</span></p><p class="ql-align-center"><span style="color: rgb(255, 255, 255);">You see a bus picking up passengers ahead. The tuk-tuk\\u2019s speed recklessly increases. Without any warning, the bus makes a sharp turn into traffic with very little speed. The bus driver doesn\\u2019t care; he assumes that every one behind him will figure it out.</span></p><p class="ql-align-center"><span style="color: rgb(255, 255, 255);">The tuk-tuk slams on the horn (before the brakes) and swings to the right, it feels like the tuk-tuk is going to flip.&nbsp;It doesn\\u2019t.&nbsp;In fact, you\\u2019re almost impressed by how well it handles jerk reactions.</span></p><p class="ql-align-center"><span style="color: rgb(255, 255, 255);">A motorcyclist is coming at you head on\\u2026 what is he doing?&nbsp;</span></p><p class="ql-align-center"><span style="color: rgb(255, 255, 255);">He gives a long, loud beep, signaling that he is not moving out of the way. Your head starts to hurt. You smacked it on the metal roof earlier when the tuk tuk violently hit a string of deep potholes.</span></p><p class="ql-align-center"><a href="https://travelingcanucks.com/wp-content/uploads/2013/07/jodhpur-rajasthan-india-06.jpg" target="_blank" style="color: rgb(255, 255, 255); background-color: inherit;"><img src="https://travelingcanucks.com/wp-content/uploads/2013/07/jodhpur-rajasthan-india-06.jpg" height="407" width="610"></a></p><h2 class="ql-align-center"><span style="color: rgb(255, 255, 255);">Are we there yet?</span></h2><p class="ql-align-center"><span style="color: rgb(255, 255, 255);">You turn the corner and are instantly stuck in a traffic jam.</span></p><p class="ql-align-center"><span style="color: rgb(255, 255, 255);">You\\u2019re trapped and not moving. Vehicles are jammed into seven lanes. The road only has three.</span></p><p class="ql-align-center"><span style="color: rgb(255, 255, 255);">The air smells like something is burning. Could there be a fire nearby? Nope,&nbsp;it\\u2019s only a pile of garbage burning on the side of the road.</span></p><p class="ql-align-center"><span style="color: rgb(255, 255, 255);">There\\u2019s a uniformed traffic officer blowing a whistle and waving a bamboo stick. He smacks a cyclist on the hand; the cyclist was trying to jump the queue. Not on his watch.&nbsp;</span></p><p class="ql-align-center"><span style="color: rgb(255, 255, 255);">You look to your left; a big brown cow is staring at you.</span></p><p class="ql-align-center"><span style="color: rgb(255, 255, 255);">Out of nowhere, a small homeless child reaches into the vehicle and grabs your arm. She points to her mouth and says \\u201cOne rupee\\u201d. The tuk tuk driver swats at her and curses something foul. He\\u2019s seen this all too often.&nbsp;</span></p><p class="ql-align-center"><a href="https://travelingcanucks.com/wp-content/uploads/2013/07/jodhpur-rajasthan-india-17.jpg" target="_blank" style="color: rgb(255, 255, 255); background-color: inherit;"><img src="https://travelingcanucks.com/wp-content/uploads/2013/07/jodhpur-rajasthan-india-17.jpg" height="409" width="610"></a></p><h3 class="ql-align-center"><span style="color: rgb(255, 255, 255);">You can\\u2019t breathe.</span></h3><p class="ql-align-center"><span style="color: rgb(255, 255, 255);">The air is&nbsp;hot, it stinks.</span></p><p class="ql-align-center"><span style="color: rgb(255, 255, 255);">The horns won\\u2019t stop beeping.</span></p><p class="ql-align-center"><span style="color: rgb(255, 255, 255);">Traffic isn\\u2019t moving.</span></p><p class="ql-align-center"><span style="color: rgb(255, 255, 255);">Then it opens up. Stop\\u2026 start. Stop\\u2026start.&nbsp;&nbsp;The tuk tuk inches its way forward.</span></p><p class="ql-align-center"><span style="color: rgb(255, 255, 255);">Then, you finally arrive at your destinations. You made it!</span></p><p class="ql-align-center"><span style="color: rgb(255, 255, 255);">The driver asks for 80 rupees\\u2026 you agreed on 50 rupees.</span></p><p class="ql-align-center"><span style="color: rgb(255, 255, 255);">Again? Seriously!</span></p><p class="ql-align-center"><a href="https://travelingcanucks.com/wp-content/uploads/2012/11/india23.jpg" target="_blank" style="color: rgb(255, 255, 255); background-color: inherit;"><img src="https://travelingcanucks.com/wp-content/uploads/2012/11/india23.jpg" height="401" width="600"></a></p><h3 class="ql-align-center"><span style="color: rgb(255, 255, 255);">When driving through the countryside the rules change.</span></h3><p class="ql-align-center"><span style="color: rgb(255, 255, 255);">The bigger the vehicle, the more damage it can cause\\u2026 thus giving it the&nbsp;</span><em style="color: rgb(255, 255, 255);">\\u2018right of way\\u2019</em><span style="color: rgb(255, 255, 255);">&nbsp;status. We typically traveled by bus on highways but were strongly encouraged by several locals to take the train instead, for safety reasons.</span></p><p class="ql-align-center"><span style="color: rgb(255, 255, 255);">Like a vehicle graveyard,&nbsp;many trucks and buses are scattered along the roads, one had even slammed into a brick wall and was abandoned.</span></p><p class="ql-align-center"><span style="color: rgb(255, 255, 255);">Trucks and buses travel in the middle of the two-lane highways at top speeds</span></p><p class="ql-align-center"><span style="color: rgb(255, 255, 255);">As they approach slower vehicles, drivers wail on the horn and pass them, regardless of the oncoming traffic. Approaching vehicles pull onto the shoulder for fear of death. Bicyclists and pedestrians are within inches of being clipped, seemingly oblivious to the danger.</span></p><p class="ql-align-center"><br></p><p class="ql-align-center"><br></p><h3 class="ql-align-center"><span style="color: rgb(255, 255, 255);">They don\\u2019t flinch\\u2026 it\\u2019s business as usual.</span></h3>', '1554295874195', '1554617413537', 1, 99),
(49, 14, 'testing thread.... (updated)', '<p>testing thread.... (updated)</p>', '1554907695354', '1554907719510', 1, 0),
(50, 14, 'testing upload image...', '<p><img src="http://localhost:3001/uploads/1554907825394_Sorano_z01.jpg"></p><p><br></p><p>testing upload image...</p>', '1554907833164', '1554907833164', 1, 0);

-- --------------------------------------------------------

--
-- Table structure for table `tags`
--

CREATE TABLE IF NOT EXISTS `tags` (
  `tid` int(6) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`tid`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=247 ;

--
-- Dumping data for table `tags`
--

INSERT INTO `tags` (`tid`, `name`) VALUES
(0, 'Popular'),
(1, 'France'),
(2, 'Afghanistan'),
(3, 'Albania'),
(4, 'Algeria'),
(5, 'American Samoa'),
(6, 'Andorra'),
(7, 'Angola'),
(8, 'Anguilla'),
(9, 'Antarctica'),
(10, 'Antigua and Barbuda'),
(11, 'Argentina'),
(12, 'Armenia'),
(13, 'Aruba'),
(14, 'Australia'),
(15, 'Austria'),
(16, 'Azerbaijan'),
(17, 'Bahamas'),
(18, 'Bahrain'),
(19, 'Bangladesh'),
(20, 'Barbados'),
(21, 'Belarus'),
(22, 'Belgium'),
(23, 'Belize'),
(24, 'Benin'),
(25, 'Bermuda'),
(26, 'Bhutan'),
(27, 'Bolivia'),
(28, 'Bosnia and Herzegovina'),
(29, 'Botswana'),
(30, 'Bouvet Island'),
(31, 'Brazil'),
(32, 'British Indian Ocean Territory'),
(33, 'Brunei Darussalam'),
(34, 'Bulgaria'),
(35, 'Burkina Faso'),
(36, 'Burundi'),
(37, 'Cambodia'),
(38, 'Cameroon'),
(39, 'Canada'),
(40, 'Cape Verde'),
(41, 'Cayman Islands'),
(42, 'Central African Republic'),
(43, 'Chad'),
(44, 'Chile'),
(45, 'China'),
(46, 'Christmas Island'),
(47, 'Cocos (Keeling) Islands'),
(48, 'Colombia'),
(49, 'Comoros'),
(50, 'Congo'),
(51, 'Cook Islands'),
(52, 'Costa Rica'),
(53, 'Croatia (Hrvatska)'),
(54, 'Cuba'),
(55, 'Cyprus'),
(56, 'Czech Republic'),
(57, 'Denmark'),
(58, 'Djibouti'),
(59, 'Dominica'),
(60, 'Dominican Republic'),
(61, 'East Timor'),
(62, 'Ecuador'),
(63, 'Egypt'),
(64, 'El Salvador'),
(65, 'Equatorial Guinea'),
(66, 'Eritrea'),
(67, 'Estonia'),
(68, 'Ethiopia'),
(69, 'Falkland Islands (Malvinas)'),
(70, 'Faroe Islands'),
(71, 'Fiji'),
(72, 'Finland'),
(73, 'France, Metropolitan'),
(74, 'French Guiana'),
(75, 'French Polynesia'),
(76, 'French Southern Territories'),
(77, 'Gabon'),
(78, 'Gambia'),
(79, 'Georgia'),
(80, 'Germany'),
(81, 'Ghana'),
(82, 'Gibraltar'),
(83, 'Guernsey'),
(84, 'Greece'),
(85, 'Greenland'),
(86, 'Grenada'),
(87, 'Guadeloupe'),
(88, 'Guam'),
(89, 'Guatemala'),
(90, 'Guinea'),
(91, 'Guinea-Bissau'),
(92, 'Guyana'),
(93, 'Haiti'),
(94, 'Heard and Mc Donald Islands'),
(95, 'Honduras'),
(96, 'Hong Kong'),
(97, 'Hungary'),
(98, 'Iceland'),
(99, 'India'),
(100, 'Isle of Man'),
(101, 'Indonesia'),
(102, 'Iran (Islamic Republic of)'),
(103, 'Iraq'),
(104, 'Ireland'),
(105, 'Israel'),
(106, 'Italy'),
(107, 'Ivory Coast'),
(108, 'Jersey'),
(109, 'Jamaica'),
(110, 'Japan'),
(111, 'Jordan'),
(112, 'Kazakhstan'),
(113, 'Kenya'),
(114, 'Kiribati'),
(115, 'Korea, Democratic People''s Republic of'),
(116, 'Korea, Republic of'),
(117, 'Kosovo'),
(118, 'Kuwait'),
(119, 'Kyrgyzstan'),
(120, 'Lao People''s Democratic Republic'),
(121, 'Latvia'),
(122, 'Lebanon'),
(123, 'Lesotho'),
(124, 'Liberia'),
(125, 'Libyan Arab Jamahiriya'),
(126, 'Liechtenstein'),
(127, 'Lithuania'),
(128, 'Luxembourg'),
(129, 'Macau'),
(130, 'Macedonia'),
(131, 'Madagascar'),
(132, 'Malawi'),
(133, 'Malaysia'),
(134, 'Maldives'),
(135, 'Mali'),
(136, 'Malta'),
(137, 'Marshall Islands'),
(138, 'Martinique'),
(139, 'Mauritania'),
(140, 'Mauritius'),
(141, 'Mayotte'),
(142, 'Mexico'),
(143, 'Micronesia, Federated States of'),
(144, 'Moldova, Republic of'),
(145, 'Monaco'),
(146, 'Mongolia'),
(147, 'Montenegro'),
(148, 'Montserrat'),
(149, 'Morocco'),
(150, 'Mozambique'),
(151, 'Myanmar'),
(152, 'Namibia'),
(153, 'Nauru'),
(154, 'Nepal'),
(155, 'Netherlands'),
(156, 'Netherlands Antilles'),
(157, 'New Caledonia'),
(158, 'New Zealand'),
(159, 'Nicaragua'),
(160, 'Niger'),
(161, 'Nigeria'),
(162, 'Niue'),
(163, 'Norfolk Island'),
(164, 'Northern Mariana Islands'),
(165, 'Norway'),
(166, 'Oman'),
(167, 'Pakistan'),
(168, 'Palau'),
(169, 'Palestine'),
(170, 'Panama'),
(171, 'Papua New Guinea'),
(172, 'Paraguay'),
(173, 'Peru'),
(174, 'Philippines'),
(175, 'Pitcairn'),
(176, 'Poland'),
(177, 'Portugal'),
(178, 'Puerto Rico'),
(179, 'Qatar'),
(180, 'Reunion'),
(181, 'Romania'),
(182, 'Russian Federation'),
(183, 'Rwanda'),
(184, 'Saint Kitts and Nevis'),
(185, 'Saint Lucia'),
(186, 'Saint Vincent and the Grenadines'),
(187, 'Samoa'),
(188, 'San Marino'),
(189, 'Sao Tome and Principe'),
(190, 'Saudi Arabia'),
(191, 'Senegal'),
(192, 'Serbia'),
(193, 'Seychelles'),
(194, 'Sierra Leone'),
(195, 'Singapore'),
(196, 'Slovakia'),
(197, 'Slovenia'),
(198, 'Solomon Islands'),
(199, 'Somalia'),
(200, 'South Africa'),
(201, 'South Georgia South Sandwich Islands'),
(202, 'South Sudan'),
(203, 'Spain'),
(204, 'Sri Lanka'),
(205, 'St. Helena'),
(206, 'St. Pierre and Miquelon'),
(207, 'Sudan'),
(208, 'Suriname'),
(209, 'Svalbard and Jan Mayen Islands'),
(210, 'Swaziland'),
(211, 'Sweden'),
(212, 'Switzerland'),
(213, 'Syrian Arab Republic'),
(214, 'Taiwan'),
(215, 'Tajikistan'),
(216, 'Tanzania, United Republic of'),
(217, 'Thailand'),
(218, 'Togo'),
(219, 'Tokelau'),
(220, 'Tonga'),
(221, 'Trinidad and Tobago'),
(222, 'Tunisia'),
(223, 'Turkey'),
(224, 'Turkmenistan'),
(225, 'Turks and Caicos Islands'),
(226, 'Tuvalu'),
(227, 'Uganda'),
(228, 'Ukraine'),
(229, 'United Arab Emirates'),
(230, 'United Kingdom'),
(231, 'United States'),
(232, 'United States minor outlying islands'),
(233, 'Uruguay'),
(234, 'Uzbekistan'),
(235, 'Vanuatu'),
(236, 'Vatican City State'),
(237, 'Venezuela'),
(238, 'Vietnam'),
(239, 'Virgin Islands (British)'),
(240, 'Virgin Islands (U.S.)'),
(241, 'Wallis and Futuna Islands'),
(242, 'Western Sahara'),
(243, 'Yemen'),
(244, 'Zaire'),
(245, 'Zambia'),
(246, 'Zimbabwe');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `uid` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `password` char(64) COLLATE utf8_unicode_ci NOT NULL,
  `email` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `admin` varchar(1) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'N',
  `time` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=15 ;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`uid`, `username`, `password`, `email`, `admin`, `time`) VALUES
(1, 'admin', '$2b$04$YghTiq/eQYNn77gLlurblezchHWY00oK3NKjaB9n75p1xqGj20hJ.', 'admin@gmail.com', 'Y', '1488539303000'),
(2, 'samjay', '01ae7e624db2a7f28761d18fac9d3fb6eaf065ee76ed814f1caae5505e6f382c', 'samtsanghksar@gmail.com', 'Y', '1488539303000'),
(3, 'majo', '0c1eb14eec5cf6cb41c2d4e23af9e83db63a1d6c142198d7cfcf37564f7deec2', 'majowong715@gmail.com', 'Y', '1488539303000'),
(4, 'test', 'e5e1d5d9258ac2f9278d00b5f1bacb5455d1c0b8032677458b06b9f65d5ace33', 'test@gmail.com', 'N', '1488539303000'),
(5, 'charlie', 'ea095f34dc2fbd568b8035d384f423968b2e8ece217bbb1734f9b4c164b4d4e4', 'wonglai1615@gmail.com', 'Y', '1488539303000'),
(6, 'sp04117', '11bb55f72a1c0c3db424208510a39c81217cd08ab033b82a8250e8cacbd0175d', 'sp04117@yahoo.com.hk', 'Y', '1488539303000'),
(7, 'tammypo', '42db73a6579cac036e79335025a9249242434f5a772cc20caacebe44505e0c42', 'tammypo00@gamil.com\n', 'N', '1488539303000'),
(8, 'testing', 'd221a2301f059c356522f1ea6e09beb6d8f1ff1f9f8adab5595d6763f406e356', 'test@ygmail.com', 'N', '1488539303000'),
(9, 'admisn', '$2b$04$p3Qx5tTxE8B1RmyvNSTJ1.0/ZQ1L/gWL6DsjOHA802a84ZpSK5Wu.', 'sda@gmail.com', 'N', '1488539303000'),
(12, 'testt', '$2b$04$o6/LYW1DWhYVeWF3dVplhOhNPeHOgzQPnv8ocDAaQRT3bPkyowsdO', 'testt', 'N', '1554640844880'),
(14, 'demo', '$2b$04$9ZHzq5K4zD1mB3nGHd.whu2pNZS/3sffOGDkEG1acu.5dnNwn07kS', 'demo@gmail.com', 'N', '1554907665359');

-- --------------------------------------------------------

--
-- Table structure for table `usersinfo`
--

CREATE TABLE IF NOT EXISTS `usersinfo` (
  `fid` int(255) NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL,
  `avatar` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `email` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `phone` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `birth` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `city` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `hobby` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `itime` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`fid`),
  UNIQUE KEY `uid` (`uid`),
  KEY `birth` (`birth`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=10 ;

--
-- Dumping data for table `usersinfo`
--

INSERT INTO `usersinfo` (`fid`, `uid`, `avatar`, `email`, `name`, `phone`, `birth`, `city`, `hobby`, `itime`) VALUES
(1, 5, '/resource/img/1499567339_20170704_180245.jpg', 'wonglai1615@gmail.com', 'WONG Lai Yin', '+852 6181 9827', NULL, 'Hong Kong', 'null', '1553865156533'),
(2, 4, '/resource/img/1499537808_default_male300x300-aae6ae0235b6cd78cee8df7ae19f6085.png', 'test@gmail.com', 'null', '+852 0000 0000', NULL, 'Hong Kong', 'null', '1553865156533'),
(3, 3, '/resource/img/1499753652_20170711_140456.jpg', 'majowong715@gmail.com', 'WONG Ho Fung', '+852 9332 4732', NULL, 'Hong Kong', 'playing', '1553865156533'),
(4, 6, '/resource/img/20170711_141559.jpg', 'sp04117@yahoo.com.hk', 'AU Sui Tai', '+852 6993 0053', NULL, 'Hong Kong', NULL, '1553865156533'),
(5, 2, '/resource/img/20170711_140738.jpg', 'samtsanghksar@gmail.com', 'TSANG Long Fung', '+852 6679 2339', NULL, 'Hong Kong', NULL, '1553865156533'),
(6, 1, '/resource/img/1500455613_5.jpg', 'admin@gmail.com', 'TSANG Long Fung', '+85266792339', NULL, 'Hong Kong', 'app development', '1554907512466'),
(7, 7, '', 'tammypo00@gamil.com\n', NULL, NULL, NULL, 'Hong Kong', NULL, '1553865156533'),
(8, 8, '', 'test@ygmail.com', NULL, NULL, NULL, NULL, NULL, '1553865156533'),
(9, 14, '', 'demo@gmail.com', NULL, NULL, NULL, NULL, NULL, '1554907670145');

--
-- Constraints for dumped tables
--

--
-- Constraints for table `comment`
--
ALTER TABLE `comment`
  ADD CONSTRAINT `comment_fk1` FOREIGN KEY (`pid`) REFERENCES `post` (`pid`),
  ADD CONSTRAINT `comment_fk2` FOREIGN KEY (`uid`) REFERENCES `users` (`uid`);

--
-- Constraints for table `post`
--
ALTER TABLE `post`
  ADD CONSTRAINT `FK_TID` FOREIGN KEY (`tid`) REFERENCES `tags` (`tid`),
  ADD CONSTRAINT `post_fk` FOREIGN KEY (`uid`) REFERENCES `users` (`uid`);

--
-- Constraints for table `usersinfo`
--
ALTER TABLE `usersinfo`
  ADD CONSTRAINT `info_fk` FOREIGN KEY (`uid`) REFERENCES `users` (`uid`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
